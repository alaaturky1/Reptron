import { useCallback, useEffect, useRef, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import * as fitnessApi from "../../api/fitnessCoachApi.js";
import { useAiCoach } from "../../context/aiCoachContext.jsx";
import { captureFrameAsJpegBase64 } from "../../features/aiCoach/framePipeline.js";
import { endSessionKeepalive } from "../../features/aiCoach/emergencyEnd.js";
import { scoreFromMistakes } from "../../features/aiCoach/scoreUtils.js";
import t from "./aiCoachTheme.module.css";

const FRAME_INTERVAL_MS = 1150;
const POLL_MS = 100;

export default function AiCoachLive() {
  const navigate = useNavigate();
  const videoRef = useRef(null);
  const streamRef = useRef(null);
  const inFlightRef = useRef(false);
  const lastSentRef = useRef(0);
  const pollTimerRef = useRef(null);
  const leftVoluntarilyRef = useRef(false);
  const sessionIdRef = useRef(null);
  const maxRepsRef = useRef(0);
  const mistakesRef = useRef([]);

  const [cameraError, setCameraError] = useState(null);
  const [cameraRetryKey, setCameraRetryKey] = useState(0);
  const [tabHidden, setTabHidden] = useState(typeof document !== "undefined" && document.hidden);

  const {
    sessionId,
    maxReps,
    movementState,
    latestErrors,
    analysisError,
    setAnalysisError,
    applyAnalyzeResponse,
    endWorkout,
    silentEndSession,
    isEnding,
    endedNormallyRef,
  } = useAiCoach();

  sessionIdRef.current = sessionId;
  maxRepsRef.current = maxReps;
  mistakesRef.current = mistakes;
  const silentEndRef = useRef(silentEndSession);
  silentEndRef.current = silentEndSession;

  const exitLiveToHome = useCallback(async () => {
    leftVoluntarilyRef.current = true;
    await silentEndSession();
    navigate("/ai", { replace: true });
  }, [navigate, silentEndSession]);

  useEffect(() => {
    function onVis() {
      setTabHidden(document.hidden);
    }
    document.addEventListener("visibilitychange", onVis);
    return () => document.removeEventListener("visibilitychange", onVis);
  }, []);

  useEffect(() => {
    if (!sessionId) return;

    let cancelled = false;

    async function openCamera() {
      setCameraError(null);
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: "user", width: { ideal: 640 }, height: { ideal: 480 } },
          audio: false,
        });
        if (cancelled) {
          stream.getTracks().forEach((tr) => tr.stop());
          return;
        }
        streamRef.current = stream;
        const v = videoRef.current;
        if (v) {
          v.srcObject = stream;
          await v.play().catch(() => {});
        }
      } catch (e) {
        const msg =
          e?.name === "NotAllowedError"
            ? "Camera permission denied. Allow camera access to use live coaching."
            : e?.name === "NotFoundError"
              ? "No camera found on this device."
              : "Could not access the camera.";
        setCameraError(msg);
      }
    }

    openCamera();

    return () => {
      cancelled = true;
      streamRef.current?.getTracks().forEach((tr) => tr.stop());
      streamRef.current = null;
    };
  }, [sessionId, cameraRetryKey]);

  useEffect(() => {
    if (!sessionId || cameraError || tabHidden) {
      if (pollTimerRef.current) {
        clearInterval(pollTimerRef.current);
        pollTimerRef.current = null;
      }
      return;
    }

    const tick = async () => {
      if (document.hidden || inFlightRef.current) return;
      const now = Date.now();
      if (now - lastSentRef.current < FRAME_INTERVAL_MS) return;

      const video = videoRef.current;
      if (!video || video.readyState < 2) return;

      const base64 = captureFrameAsJpegBase64(video, 480, 0.5);
      if (!base64) return;

      inFlightRef.current = true;
      lastSentRef.current = now;

      try {
        const res = await fitnessApi.analyzeFrame({
          sessionId,
          frameBase64: base64,
          mimeType: "image/jpeg",
          timestamp: now,
        });
        applyAnalyzeResponse(res.reps, res.state, res.errors);
      } catch (e) {
        const msg = e?.response?.data?.message || e?.message || "Analysis request failed.";
        setAnalysisError(msg);
      } finally {
        inFlightRef.current = false;
      }
    };

    pollTimerRef.current = setInterval(tick, POLL_MS);
    return () => {
      if (pollTimerRef.current) {
        clearInterval(pollTimerRef.current);
        pollTimerRef.current = null;
      }
    };
  }, [sessionId, cameraError, tabHidden, applyAnalyzeResponse, setAnalysisError]);

  useEffect(() => {
    function fireKeepalive() {
      if (endedNormallyRef.current || leftVoluntarilyRef.current) return;
      const sid = sessionIdRef.current;
      if (!sid) return;
      endSessionKeepalive({
        sessionId: sid,
        reps: maxRepsRef.current,
        score: scoreFromMistakes(mistakesRef.current),
        mistakes: mistakesRef.current,
      });
    }

    function onPageHide() {
      if (endedNormallyRef.current || leftVoluntarilyRef.current) return;
      fireKeepalive();
    }
    window.addEventListener("pagehide", onPageHide);

    return () => {
      window.removeEventListener("pagehide", onPageHide);
      if (endedNormallyRef.current || leftVoluntarilyRef.current) return;
      fireKeepalive();
      silentEndRef.current();
    };
  }, []);

  if (!sessionId) {
    return <Navigate to="/ai" replace />;
  }

  const hasFormIssues = (latestErrors?.length ?? 0) > 0;
  const normalizedState = (movementState || "—").trim() || "—";

  return (
    <div className="container" style={{ maxWidth: 560 }}>
      <div className={t.hudWrap}>
        <video ref={videoRef} className={t.video} playsInline muted autoPlay />
        <div className={t.hudOverlay} />
        <div className={t.hudTop}>
          <button type="button" className={t.backBtn} onClick={exitLiveToHome} aria-label="Back and end session">
            <i className="fas fa-arrow-left" />
          </button>
          <div className={t.hudPanel} style={{ flex: 1, maxWidth: "72%" }}>
            <div className={t.hudReps}>{maxReps}</div>
            <div className={t.hudState}>Reps · max</div>
            <div className={t.hudState} style={{ marginTop: "0.35rem", color: "#e2e8f0" }}>
              State: {normalizedState}
            </div>
          </div>
        </div>

        <div className={t.hudBottom}>
          {cameraError ? (
            <div className={t.errorBanner} style={{ pointerEvents: "auto" }}>
              {cameraError}
              <div className="mt-2">
                <button
                  type="button"
                  className={t.btnSecondary}
                  style={{ maxWidth: "100%", fontSize: "0.85rem", padding: "0.5rem" }}
                  onClick={() => {
                    setCameraError(null);
                    setCameraRetryKey((k) => k + 1);
                  }}
                >
                  Retry camera
                </button>
              </div>
            </div>
          ) : null}

          {!cameraError && hasFormIssues ? (
            <ul className={t.errList}>
              {latestErrors.map((err, i) => (
                <li key={`${err}-${i}`}>{err}</li>
              ))}
            </ul>
          ) : null}

          {!cameraError && !hasFormIssues ? (
            <div className={t.goodForm}>
              <i className="fas fa-circle-check" />
              Good form
            </div>
          ) : null}

          {analysisError ? <div className={t.networkWarn}>{analysisError}</div> : null}
          {tabHidden ? (
            <div className={t.networkWarn} style={{ marginTop: "0.5rem" }}>
              Tab in background — analysis paused.
            </div>
          ) : null}

          <button type="button" className={t.endBtn} disabled={isEnding || !!cameraError} onClick={() => endWorkout()}>
            {isEnding ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" />
                Ending…
              </>
            ) : (
              <>
                <i className="fas fa-stop me-2" />
                End Workout
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
}
