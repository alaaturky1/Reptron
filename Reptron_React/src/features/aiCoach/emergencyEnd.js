import { API_BASE_URL } from "../../config/env.js";
import { AUTH_TOKEN_KEY } from "../../api/httpClient.js";

/**
 * Best-effort end-session when tab closes (Authorization + JSON body).
 * Does not await; safe for beforeunload/pagehide.
 */
export function endSessionKeepalive({ sessionId, reps, score, mistakes }) {
  if (!sessionId) return;
  try {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    fetch(`${API_BASE_URL}/api/FitnessCoach/end-session`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        sessionId,
        reps: Math.max(0, Number(reps) || 0),
        score,
        mistakes: Array.isArray(mistakes) ? mistakes : [],
      }),
      keepalive: true,
    }).catch(() => {});
  } catch {
    /* ignore */
  }
}
