import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import * as fitnessApi from "../../api/fitnessCoachApi.js";
import {
  loadSessions,
  saveAllSessions,
} from "../../features/aiCoach/sessionStorage.js";
import t from "./aiCoachTheme.module.css";

export default function AiCoachHistory() {
  const navigate = useNavigate();
  const [sessions, setSessions] = useState(() => loadSessions());
  const [syncing, setSyncing] = useState(false);
  const [touchStart, setTouchStart] = useState(null);

  const syncWithServer = useCallback(async () => {
    const list = loadSessions();
    setSyncing(true);
    const next = [...list];
    for (let i = 0; i < next.length; i++) {
      const sid = next[i].serverSessionId;
      if (!sid) continue;
      try {
        const data = await fitnessApi.getSessionSummary(sid);
        if (data && typeof data === "object") {
          next[i] = {
            ...next[i],
            reps: data.reps ?? data.repCount ?? next[i].reps,
            score: data.score ?? next[i].score,
            mistakes: Array.isArray(data.mistakes) ? data.mistakes : next[i].mistakes,
            feedback: data.feedback ?? data.coachFeedback ?? next[i].feedback,
          };
        }
      } catch {
        /* keep local row */
      }
    }
    saveAllSessions(next);
    setSessions(next);
    setSyncing(false);
  }, []);

  useEffect(() => {
    syncWithServer();
  }, [syncWithServer]);

  function onTouchStart(e) {
    setTouchStart(e.targetTouches[0].clientY);
  }

  function onTouchEnd(e) {
    if (touchStart == null) return;
    const y = e.changedTouches[0].clientY;
    if (y - touchStart > 70) {
      syncWithServer();
    }
    setTouchStart(null);
  }

  return (
    <div
      className="container"
      style={{ maxWidth: 520 }}
      onTouchStart={onTouchStart}
      onTouchEnd={onTouchEnd}
    >
      <button type="button" className={`${t.backBtn} mb-3`} onClick={() => navigate("/ai")} aria-label="Back">
        <i className="fas fa-arrow-left" />
      </button>

      <h1 className={t.titleXL}>Workout History</h1>
      <p className={t.subtitle}>
        {syncing ? "Syncing with server…" : "Pull down on touch devices to refresh summaries."}
      </p>

      <button
        type="button"
        className={`${t.btnSecondary} mb-4`}
        style={{ maxWidth: "100%" }}
        disabled={syncing}
        onClick={syncWithServer}
      >
        <i className={`fas fa-rotate ${syncing ? "fa-spin" : ""}`} />
        Refresh
      </button>

      {sessions.length === 0 ? (
        <div className={`${t.glassStrong} p-5 text-center`}>
          <div className={t.emptyIllustration}>
            <i className="fas fa-inbox" />
          </div>
          <p className="mb-0" style={{ color: "var(--ai-muted)" }}>
            No saved sessions yet. Complete a workout to build your history.
          </p>
        </div>
      ) : (
        sessions.map((s) => (
          <div key={s.id} className={`${t.glass} ${t.historyRow}`}>
            <div>
              <div className={t.historyDate}>{new Date(s.date).toLocaleString()}</div>
              <div style={{ fontWeight: 600, marginTop: "0.25rem" }}>Session</div>
            </div>
            <div className="text-end">
              <div className={t.historyMeta}>{s.reps} reps</div>
              <div style={{ fontSize: "0.9rem", color: "var(--ai-muted)" }}>Score {s.score}</div>
            </div>
          </div>
        ))
      )}
    </div>
  );
}
