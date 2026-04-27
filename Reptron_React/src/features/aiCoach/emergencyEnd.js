import { API_BASE_URL } from "../../config/env.js";
import { AUTH_TOKEN_KEY } from "../../api/httpClient.js";

/**
 * Best-effort end-session when tab closes (Authorization + JSON body).
 * Does not await; safe for beforeunload/pagehide.
 */
export function endSessionKeepalive({ sessionId }) {
  if (!sessionId) return;
  try {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    fetch(`${API_BASE_URL}/api/fitness-coach/end-session`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        sessionId,
      }),
      keepalive: true,
    }).catch(() => {});
  } catch {
    /* ignore */
  }
}
