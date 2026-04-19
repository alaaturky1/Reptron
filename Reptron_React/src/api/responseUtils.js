/**
 * Many list endpoints return OData-style envelopes: { value: [...], Count: n }.
 */
export function unwrapListPayload(data) {
  if (!data) return [];
  if (Array.isArray(data)) return data;
  if (Array.isArray(data.value)) return data.value;
  if (Array.isArray(data.data)) return data.data;
  if (Array.isArray(data.items)) return data.items;
  if (Array.isArray(data.result)) return data.result;
  return [];
}

/**
 * Pull JWT from common ASP.NET / Identity response shapes.
 */
export function extractAuthToken(payload) {
  if (payload == null || typeof payload !== "object") return null;
  return (
    payload.token ||
    payload.accessToken ||
    payload.access_token ||
    payload.data?.token ||
    payload.result?.token ||
    (typeof payload.data === "string" ? payload.data : null)
  );
}

/**
 * Map axios errors and ASP.NET ProblemDetails to a user-facing string.
 */
export function getApiErrorMessage(error) {
  const res = error?.response;
  const data = res?.data;

  if (typeof data === "string" && data.trim()) return data;

  if (data && typeof data === "object") {
    if (typeof data.detail === "string" && data.detail.trim()) return data.detail;
    if (typeof data.title === "string" && data.title.trim()) return data.title;
    if (typeof data.message === "string" && data.message.trim()) return data.message;

    const errs = data.errors;
    if (errs && typeof errs === "object") {
      const parts = Object.values(errs).flat().filter(Boolean);
      if (parts.length) return parts.join(" ");
    }
  }

  if (res?.status === 401) return "Please sign in to continue.";
  if (res?.status === 403) return "You do not have permission to perform this action.";
  if (res?.status === 404) return "The requested resource was not found.";

  if (error?.code === "ECONNABORTED") return "Request timed out. Check your connection and try again.";
  if (typeof error?.message === "string" && error.message !== "Network Error") return error.message;
  if (error?.message === "Network Error") return "Network error. Please check your connection.";

  return "Something went wrong. Please try again.";
}
