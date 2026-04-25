import { useMemo } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { getDisplayName, useAiCoach } from "../../context/aiCoachContext.jsx";
import { loadSessions } from "../../features/aiCoach/sessionStorage.js";
import t from "./aiCoachTheme.module.css";

export default function AiCoachHome() {
  const navigate = useNavigate();
  const { startWorkout, isStarting, analysisError, setAnalysisError } = useAiCoach();

  const greeting = useMemo(() => {
    const name = getDisplayName();
    return name ? `Welcome back, ${name}` : "Welcome";
  }, []);

  const last = useMemo(() => {
    const list = loadSessions();
    return list[0] ?? null;
  }, []);

  async function onStart() {
    setAnalysisError(null);
    try {
      await startWorkout();
    } catch {
      toast.error("Could not start session.");
    }
  }

  return (
    <div className="container" style={{ maxWidth: 480 }}>
      <p className={t.pill}>
        <i className="fas fa-robot" aria-hidden />
        {greeting}
      </p>
      <h1 className={t.titleXL}>AI Fitness Coach</h1>
      <p className={t.subtitle}>
        Live camera pose analysis, rep counting, and form feedback — optimized for a smooth, app-like flow.
      </p>

      {analysisError ? <div className={`${t.errorBanner} mb-3`}>{analysisError}</div> : null}

      <div className={`${t.glassStrong} p-4 mb-4`}>
        <h2 className="h6 text-uppercase mb-3" style={{ color: "var(--ai-muted)", letterSpacing: "0.08em" }}>
          Last session
        </h2>
        {last ? (
          <>
            <div className="d-flex justify-content-between align-items-baseline mb-2">
              <span style={{ color: "var(--ai-muted)", fontSize: "0.85rem" }}>Reps</span>
              <span style={{ fontSize: "1.5rem", fontWeight: 800, color: "var(--ai-cyan)" }}>{last.reps}</span>
            </div>
            <div className="d-flex justify-content-between align-items-baseline mb-2">
              <span style={{ color: "var(--ai-muted)", fontSize: "0.85rem" }}>Score</span>
              <span style={{ fontSize: "1.25rem", fontWeight: 700 }}>{last.score}</span>
            </div>
            <p className="mb-0" style={{ color: "var(--ai-muted)", fontSize: "0.8rem" }}>
              {new Date(last.date).toLocaleString()}
            </p>
          </>
        ) : (
          <p className="mb-0" style={{ color: "var(--ai-muted)" }}>
            No workouts yet — start your first session to see stats here.
          </p>
        )}
      </div>

      <div className={t.stack}>
        <button type="button" className={t.btnPrimary} disabled={isStarting} onClick={onStart}>
          {isStarting ? (
            <>
              <span className="spinner-border spinner-border-sm" role="status" />
              Starting…
            </>
          ) : (
            <>
              <i className="fas fa-play" />
              Start Workout
            </>
          )}
        </button>
        <button type="button" className={t.btnSecondary} onClick={() => navigate("/ai/history")}>
          <i className="fas fa-clock-rotate-left" />
          Workout History
        </button>
      </div>
    </div>
  );
}
