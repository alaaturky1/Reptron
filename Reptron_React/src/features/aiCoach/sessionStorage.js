const STORAGE_KEY = "aiFitnessCoachSessions";
const LAST_RESULT_KEY = "aiFitnessCoachLastResult";

function safeParse(json, fallback) {
  try {
    return JSON.parse(json);
  } catch {
    return fallback;
  }
}

/** @typedef {{ id: string, date: string, reps: number, score: number, mistakes: string[], feedback?: string, serverSessionId?: string }} AiSessionRecord */

/**
 * @returns {AiSessionRecord[]}
 */
export function loadSessions() {
  const raw = localStorage.getItem(STORAGE_KEY);
  const parsed = safeParse(raw, []);
  return Array.isArray(parsed) ? parsed : [];
}

/**
 * @param {AiSessionRecord} record
 */
export function appendSession(record) {
  const list = loadSessions();
  list.unshift(record);
  localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
}

/**
 * @param {string} id
 * @param {Partial<AiSessionRecord>} patch
 */
export function updateSessionById(id, patch) {
  const list = loadSessions();
  const idx = list.findIndex((s) => s.id === id);
  if (idx === -1) return;
  list[idx] = { ...list[idx], ...patch };
  localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
}

/**
 * Replace list (after bulk sync).
 * @param {AiSessionRecord[]} sessions
 */
export function saveAllSessions(sessions) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(sessions));
}

export function saveLastResultPayload(payload) {
  sessionStorage.setItem(LAST_RESULT_KEY, JSON.stringify(payload));
}

export function loadLastResultPayload() {
  const raw = sessionStorage.getItem(LAST_RESULT_KEY);
  return raw ? safeParse(raw, null) : null;
}

export function clearLastResultPayload() {
  sessionStorage.removeItem(LAST_RESULT_KEY);
}
