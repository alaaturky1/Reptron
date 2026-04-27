import httpClient from "./httpClient.js";

/** Swagger routes use kebab-case: `/api/fitness-coach/...` (PascalCase returns 404 on host). */
const BASE = "/api/FitnessCoach";

/**
 * POST /api/fitness-coach/start-session
 * @returns {{ sessionId: string, raw: object }}
 */
export async function startSession() {
  const { data } = await httpClient.post(`${BASE}/start-session`, {});
  const id = data?.sessionId ?? data?.session_id ?? data?.id;
  if (id == null || id === "") {
    throw new Error("Start session failed: no session id in response.");
  }
  return { sessionId: String(id), raw: data };
}

/**
 * POST /api/fitness-coach/analyze-frame
 * Body matches Swagger: { sessionId, frame: { imageB64, timestamp, ... } }.
 */
export async function analyzeFrame({ sessionId, frameBase64, mimeType = "image/jpeg", timestamp }) {
  void mimeType;
  const { data } = await httpClient.post(`${BASE}/analyze-frame`, {
    sessionId,
    frame: {
      imageB64: frameBase64,
      timestamp: typeof timestamp === "number" ? timestamp : Date.now(),
    },
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
 * POST /api/fitness-coach/end-session
 * Swagger EndSessionRequestDto: only sessionId (additionalProperties: false).
 */
export async function endSession({ sessionId }) {
  const { data } = await httpClient.post(`${BASE}/end-session`, {
    sessionId,
  });
  return data ?? {};
}

/**
 * GET /api/fitness-coach/session-summary/{sessionId}
 */
export async function getSessionSummary(sessionId) {
  const { data } = await httpClient.get(`${BASE}/session-summary/${encodeURIComponent(sessionId)}`);
  return data;
}
