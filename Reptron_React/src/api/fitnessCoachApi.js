import httpClient from "./httpClient.js";
import { AI_API_BASE_URL, AI_API_KEY, AI_API_PREFIX } from "../config/env.js";

function aiPath(path) {
  const base = AI_API_BASE_URL.replace(/\/+$/, "");
  return `${base}${AI_API_PREFIX}${path}`;
}

function withAiHeaders(extra = {}) {
  return AI_API_KEY ? { ...extra, "X-API-Key": AI_API_KEY } : extra;
}

/**
 * POST /start-session
 * @returns {{ sessionId: string, raw: object }}
 */
export async function startSession({ language = "en", level = "beginner" } = {}) {
  const { data } = await httpClient.post(
    aiPath("/start-session"),
    { language, level },
    { headers: withAiHeaders() }
  );
  const id = data?.sessionId ?? data?.session_id ?? data?.id;
  if (id == null || id === "") {
    throw new Error("Start session failed: no session id in response.");
  }
  return { sessionId: String(id), wsUrl: data?.ws_url ?? data?.wsUrl ?? null, raw: data };
}

/**
 * POST /analyze-frame
 */
export async function analyzeFrame({ sessionId, frameBase64, timestamp, frame, exercise = "squat" }) {
  const ts = Number(timestamp);
  const tsSeconds = Number.isFinite(ts) ? ts / 1000 : Date.now() / 1000;
  const framePayload =
    frame && typeof frame === "object"
      ? { ...frame, timestamp: Number.isFinite(Number(frame.timestamp)) ? Number(frame.timestamp) : tsSeconds }
      : {
          timestamp: tsSeconds,
          exercise,
          image_b64: frameBase64 || null,
        };
  const { data } = await httpClient.post(
    aiPath("/analyze-frame"),
    {
      session_id: String(sessionId),
      frame: framePayload,
    },
    { headers: withAiHeaders() }
  );

  const repsRaw = data?.repCount ?? data?.rep_count ?? data?.reps ?? 0;
  const phase = data?.debug?.phase;
  const stateRaw = data?.state ?? data?.movementState ?? data?.movement_state ?? phase ?? (data?.paused ? "paused" : "");
  const errorsRaw = data?.issues ?? data?.detectedErrors ?? data?.detected_errors ?? data?.errors;

  let errors = [];
  if (Array.isArray(errorsRaw)) {
    errors = errorsRaw.map((e) => (typeof e === "string" ? e : e?.message ?? String(e))).filter(Boolean);
  } else if (errorsRaw != null && errorsRaw !== "") {
    errors = [String(errorsRaw)];
  }

  return {
    reps: Math.max(0, Number(repsRaw) || 0),
    state: String(stateRaw ?? ""),
    errors,
    raw: data,
  };
}

/**
 * POST /end-session
 */
export async function endSession({ sessionId, reps, score, mistakes }) {
  const { data } = await httpClient.post(
    aiPath("/end-session"),
    { session_id: String(sessionId) },
    { headers: withAiHeaders() }
  );
  return {
    ...data,
    reps: data?.reps ?? Math.max(0, Number(reps) || 0),
    score: data?.avg_rep_score ?? score ?? null,
    mistakes:
      Array.isArray(data?.mistakes)
        ? data.mistakes
        : Array.isArray(mistakes)
          ? mistakes
          : [],
    feedback: data?.feedback ?? data?.coachFeedback ?? null,
  };
}

/**
 * GET /session-summary/{sessionId}
 */
export async function getSessionSummary(sessionId) {
  const { data } = await httpClient.get(aiPath(`/session-summary/${encodeURIComponent(sessionId)}`), {
    headers: withAiHeaders(),
  });
  const issuesTally = data?.issues_tally && typeof data.issues_tally === "object" ? data.issues_tally : {};
  const topMistakes = Object.entries(issuesTally)
    .filter(([issue]) => issue !== "visibility_low" && issue !== "unknown_exercise")
    .sort((a, b) => Number(b[1] || 0) - Number(a[1] || 0))
    .slice(0, 3)
    .map(([issue]) => issue);

  return {
    ...data,
    reps: data?.reps ?? 0,
    score: data?.avg_rep_score ?? null,
    mistakes: topMistakes,
    feedback: data?.feedback ?? null,
  };
}
