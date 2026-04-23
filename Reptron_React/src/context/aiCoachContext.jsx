import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useRef,
  useState,
} from "react";
import { useNavigate } from "react-router-dom";
import * as fitnessApi from "../api/fitnessCoachApi.js";
import {
  appendSession,
  clearLastResultPayload,
  loadLastResultPayload,
  saveLastResultPayload,
} from "../features/aiCoach/sessionStorage.js";
import { mergeMistakes, scoreFromMistakes } from "../features/aiCoach/scoreUtils.js";

const AiCoachContext = createContext(null);

const USER_PROFILE_KEY = "userProfile";
const DEFAULT_EXERCISE = "squat";

export function getDisplayName() {
  try {
    const p = JSON.parse(localStorage.getItem(USER_PROFILE_KEY) || "{}");
    return (p.name || "").trim() || null;
  } catch {
    return null;
  }
}

function buildFallbackFeedback({ reps, score, mistakes }) {
  const m = mistakes?.length ?? 0;
  if (m === 0) {
    return `Strong session — ${reps} reps with a score of ${score}. Keep this consistency.`;
  }
  return `You logged ${reps} reps with a score of ${score}. Focus next time on: ${mistakes.slice(0, 3).join("; ")}${m > 3 ? "…" : ""}`;
}

export function AiCoachProvider({ children }) {
  const navigate = useNavigate();
  const endedNormallyRef = useRef(false);

  const [sessionId, setSessionId] = useState(null);
  const [selectedExercise, setSelectedExercise] = useState(DEFAULT_EXERCISE);
  const [maxReps, setMaxReps] = useState(0);
  const [movementState, setMovementState] = useState("");
  const [latestErrors, setLatestErrors] = useState([]);
  const [mistakes, setMistakes] = useState([]);
  const [analysisError, setAnalysisError] = useState(null);
  const [isStarting, setIsStarting] = useState(false);
  const [isEnding, setIsEnding] = useState(false);
  const [lastResult, setLastResult] = useState(null);

  const resetLiveFields = useCallback(() => {
    setSessionId(null);
    setMaxReps(0);
    setMovementState("");
    setLatestErrors([]);
    setMistakes([]);
    setAnalysisError(null);
  }, []);

  const applyAnalyzeResponse = useCallback((reps, state, errors) => {
    setMaxReps((prev) => Math.max(prev, reps));
    setMovementState(state || "—");
    const errList = Array.isArray(errors) ? errors : [];
    setLatestErrors(errList);
    setMistakes((prev) => mergeMistakes(prev, errList));
    setAnalysisError(null);
  }, []);

  const startWorkout = useCallback(async ({ exercise, language = "en", level = "beginner" } = {}) => {
    setIsStarting(true);
    setAnalysisError(null);
    endedNormallyRef.current = false;
    try {
      const normalizedExercise = typeof exercise === "string" && exercise.trim() ? exercise.trim().toLowerCase() : selectedExercise;
      const { sessionId: sid } = await fitnessApi.startSession({ language, level });
      setMaxReps(0);
      setMovementState("");
      setLatestErrors([]);
      setMistakes([]);
      setSelectedExercise(normalizedExercise);
      setSessionId(sid);
      navigate("/ai/live", { replace: true });
    } catch (e) {
      const msg = e?.response?.data?.message || e?.message || "Could not start workout session.";
      setAnalysisError(msg);
      throw e;
    } finally {
      setIsStarting(false);
    }
  }, [navigate, selectedExercise]);

  const tryAgainFromResult = useCallback(async () => {
    clearLastResultPayload();
    setLastResult(null);
    await startWorkout({ exercise: selectedExercise });
  }, [startWorkout, selectedExercise]);

  const finalizeEndOnServer = useCallback(
    async (sid, reps, mistakeList) => {
      const score = scoreFromMistakes(mistakeList);
      const data = await fitnessApi.endSession({
        sessionId: sid,
        reps,
        score,
        mistakes: mistakeList,
      });
      return { score, feedback: data?.feedback ?? data?.coachFeedback ?? data?.message ?? null };
    },
    []
  );

  const endWorkout = useCallback(async () => {
    if (!sessionId) return;
    setIsEnding(true);
    setAnalysisError(null);
    try {
      const reps = maxReps;
      const mistakeList = [...mistakes];
      const { score, feedback } = await finalizeEndOnServer(sessionId, reps, mistakeList);
      const fb =
        (typeof feedback === "string" && feedback.trim()) ||
        buildFallbackFeedback({ reps, score, mistakes: mistakeList });

      const record = {
        id: `local-${Date.now()}`,
        date: new Date().toISOString(),
        exercise: selectedExercise,
        reps,
        score,
        mistakes: mistakeList,
        feedback: fb,
        serverSessionId: sessionId,
      };
      appendSession(record);

      const resultPayload = {
        ...record,
        sessionId,
      };
      setLastResult(resultPayload);
      saveLastResultPayload(resultPayload);
      endedNormallyRef.current = true;
      resetLiveFields();
      navigate("/ai/result", { replace: true });
    } catch (e) {
      const msg = e?.response?.data?.message || e?.message || "Failed to end session.";
      setAnalysisError(msg);
    } finally {
      setIsEnding(false);
    }
  }, [sessionId, maxReps, mistakes, finalizeEndOnServer, navigate, resetLiveFields, selectedExercise]);

  const silentEndSession = useCallback(async () => {
    const sid = sessionId;
    if (!sid) return;
    const reps = maxReps;
    const mistakeList = [...mistakes];
    const score = scoreFromMistakes(mistakeList);
    try {
      await fitnessApi.endSession({
        sessionId: sid,
        reps,
        score,
        mistakes: mistakeList,
      });
    } catch {
      /* fail-safe: still clear local session */
    }
    resetLiveFields();
  }, [sessionId, maxReps, mistakes, resetLiveFields]);

  const markEndedNormally = useCallback(() => {
    endedNormallyRef.current = true;
  }, []);

  const hydrateResultFromStorage = useCallback(() => {
    const p = loadLastResultPayload();
    if (p) setLastResult(p);
  }, []);

  const clearResult = useCallback(() => {
    clearLastResultPayload();
    setLastResult(null);
  }, []);

  const value = useMemo(
    () => ({
      sessionId,
      selectedExercise,
      maxReps,
      movementState,
      latestErrors,
      mistakes,
      analysisError,
      isStarting,
      isEnding,
      lastResult,
      setAnalysisError,
      applyAnalyzeResponse,
      startWorkout,
      setSelectedExercise,
      endWorkout,
      silentEndSession,
      resetLiveFields,
      tryAgainFromResult,
      endedNormallyRef,
      markEndedNormally,
      hydrateResultFromStorage,
      clearResult,
    }),
    [
      sessionId,
      selectedExercise,
      maxReps,
      movementState,
      latestErrors,
      mistakes,
      analysisError,
      isStarting,
      isEnding,
      lastResult,
      setAnalysisError,
      applyAnalyzeResponse,
      startWorkout,
      setSelectedExercise,
      endWorkout,
      silentEndSession,
      resetLiveFields,
      tryAgainFromResult,
      markEndedNormally,
      hydrateResultFromStorage,
      clearResult,
    ]
  );

  return <AiCoachContext.Provider value={value}>{children}</AiCoachContext.Provider>;
}

export function useAiCoach() {
  const ctx = useContext(AiCoachContext);
  if (!ctx) throw new Error("useAiCoach must be used within AiCoachProvider");
  return ctx;
}

export { buildFallbackFeedback };
