import { AI_API_BASE_URL, AI_API_KEY, AI_API_PREFIX } from "../../config/env.js";
import { AUTH_TOKEN_KEY } from "../../api/httpClient.js";

/**
 * Best-effort end-session when tab closes (Authorization + JSON body).
 * Does not await; safe for beforeunload/pagehide.
 */
export function endSessionKeepalive({ sessionId, reps, score, mistakes }) {
  if (!sessionId) return;
  try {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    const prefix = AI_API_PREFIX || "";
    fetch(`${AI_API_BASE_URL}${prefix}/end-session`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(AI_API_KEY ? { "X-API-Key": AI_API_KEY } : {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        session_id: String(sessionId),
        reps: Math.max(0, Number(reps) || 0), // ignored by current backend, kept for forward compatibility
        score, // ignored by current backend, kept for forward compatibility
        mistakes: Array.isArray(mistakes) ? mistakes : [], // ignored by current backend
      }),
      keepalive: true,
    }).catch(() => {});
  } catch {
    /* ignore */
  }
}
