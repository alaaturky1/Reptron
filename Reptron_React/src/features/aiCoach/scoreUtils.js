/** @param {string[]} mistakes */
export function scoreFromMistakes(mistakes) {
  const n = Array.isArray(mistakes) ? mistakes.length : 0;
  return Math.max(0, Math.min(100, 100 - n * 10));
}

/** Merge unique mistake strings preserving order */
export function mergeMistakes(existing, incoming) {
  const set = new Set(existing ?? []);
  for (const m of incoming ?? []) {
    const s = String(m).trim();
    if (s) set.add(s);
  }
  return [...set];
}
