import httpClient from "./httpClient.js";

/**
 * POST /api/FitnessCoach/start-session
 * @returns {{ sessionId: string, raw: object }}
 */
export async function startSession() {
  const { data } = await httpClient.post("/api/FitnessCoach/start-session", {});
  const id = data?.sessionId ?? data?.session_id ?? data?.id;
  if (id == null || id === "") {
    throw new Error("Start session failed: no session id in response.");
  }
  return { sessionId: String(id), raw: data };
}

/**
 * POST /api/FitnessCoach/analyze-frame
 */
export async function analyzeFrame({ sessionId, frameBase64, mimeType = "image/jpeg", timestamp }) {
  const { data } = await httpClient.post("/api/FitnessCoach/analyze-frame", {
    sessionId,
    frameBase64,
    mimeType,
    timestamp,
  });

  const repsRaw = data?.repCount ?? data?.rep_count ?? data?.reps ?? 0;
  const stateRaw = data?.state ?? data?.movementState ?? data?.movement_state ?? "";
  const errorsRaw = data?.detectedErrors ?? data?.detected_errors ?? data?.errors;

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
 * POST /api/FitnessCoach/end-session
 */
export async function endSession({ sessionId, reps, score, mistakes }) {
  const { data } = await httpClient.post("/api/FitnessCoach/end-session", {
    sessionId,
    reps,
    score,
    mistakes,
  });
  return data ?? {};
}

/**
 * GET /api/FitnessCoach/session-summary/{sessionId}
 */
export async function getSessionSummary(sessionId) {
  const { data } = await httpClient.get(`/api/FitnessCoach/session-summary/${encodeURIComponent(sessionId)}`);
  return data;
}
