import { useEffect, useMemo } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { buildFallbackFeedback, useAiCoach } from "../../context/aiCoachContext.jsx";
import { loadLastResultPayload } from "../../features/aiCoach/sessionStorage.js";
import t from "./aiCoachTheme.module.css";

export default function AiCoachResult() {
  const navigate = useNavigate();
  const { lastResult, hydrateResultFromStorage, tryAgainFromResult, isStarting, clearResult } = useAiCoach();

  useEffect(() => {
    hydrateResultFromStorage();
  }, [hydrateResultFromStorage]);

  const data = useMemo(() => lastResult || loadLastResultPayload(), [lastResult]);

  if (!data) {
    return <Navigate to="/ai" replace />;
  }

  const mistakes = Array.isArray(data.mistakes) ? data.mistakes : [];
  const exercise = typeof data.exercise === "string" && data.exercise.trim() ? data.exercise : "squat";
  const feedback =
    (typeof data.feedback === "string" && data.feedback.trim()) ||
    buildFallbackFeedback({ reps: data.reps, score: data.score, mistakes });

  async function onTryAgain() {
    try {
      await tryAgainFromResult();
    } catch {
      toast.error("Could not start a new session.");
    }
  }

  function onHome() {
    clearResult();
    navigate("/ai", { replace: true });
  }

  return (
    <div className="container" style={{ maxWidth: 480 }}>
      <h1 className={t.titleXL}>Workout Complete</h1>
      <p className={t.subtitle}>Here is how this session shaped up.</p>

      <div className={`${t.glassStrong} p-4 mb-3`}>
        <div className="d-flex justify-content-between mb-3">
          <div>
            <div style={{ color: "var(--ai-muted)", fontSize: "0.8rem" }}>Exercise</div>
            <div style={{ fontSize: "1rem", fontWeight: 700, textTransform: "capitalize" }}>{exercise}</div>
          </div>
          <div>
            <div style={{ color: "var(--ai-muted)", fontSize: "0.8rem" }}>Reps</div>
            <div style={{ fontSize: "2rem", fontWeight: 800, color: "var(--ai-cyan)" }}>{data.reps}</div>
          </div>
          <div className="text-end">
            <div style={{ color: "var(--ai-muted)", fontSize: "0.8rem" }}>Score</div>
            <div style={{ fontSize: "2rem", fontWeight: 800 }}>{data.score}</div>
          </div>
        </div>
        <p className="mb-0" style={{ color: "var(--ai-muted)", fontSize: "0.8rem" }}>
          {data.date ? new Date(data.date).toLocaleString() : ""}
        </p>
      </div>

      <div className={`${t.glass} p-4 mb-3`}>
        <h2 className="h6 text-uppercase mb-2" style={{ color: "var(--ai-muted)", letterSpacing: "0.06em" }}>
          Detected issues
        </h2>
        {mistakes.length === 0 ? (
          <p className="mb-0" style={{ color: "var(--ai-good)", fontWeight: 600 }}>
            No issues — great job
          </p>
        ) : (
          <ul className="mb-0 ps-3" style={{ color: "#fecaca" }}>
            {mistakes.map((m, i) => (
              <li key={`${m}-${i}`}>{m}</li>
            ))}
          </ul>
        )}
      </div>

      <div className={`${t.glass} p-4 mb-4`}>
        <h2 className="h6 text-uppercase mb-2" style={{ color: "var(--ai-muted)", letterSpacing: "0.06em" }}>
          Coach feedback
        </h2>
        <p className="mb-0" style={{ lineHeight: 1.55 }}>
          {feedback}
        </p>
      </div>

      <div className={t.stack}>
        <button type="button" className={t.btnPrimary} disabled={isStarting} onClick={onTryAgain}>
          {isStarting ? (
            <>
              <span className="spinner-border spinner-border-sm" role="status" />
              Starting…
            </>
          ) : (
            <>
              <i className="fas fa-redo" />
              Try Again
            </>
          )}
        </button>
        <button type="button" className={t.btnSecondary} onClick={onHome}>
          <i className="fas fa-house" />
          Back to coach home
        </button>
      </div>
    </div>
  );
}
