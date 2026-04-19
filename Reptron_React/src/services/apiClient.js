/**
 * Back-compat re-exports — prefer importing from `src/api/httpClient.js` and `src/config/env.js`.
 */
export { default, AUTH_TOKEN_KEY, LOCAL_AUTH_PASSWORD_KEY } from "../api/httpClient.js";
export { API_BASE_URL } from "../config/env.js";
