/**
 * Tiny in-memory TTL cache for GET-heavy data (product lists/details).
 * Invalidates entries after maxAgeMs or when clear() is called (e.g. after login).
 */

const store = new Map();

export function getCached(key) {
  const entry = store.get(key);
  if (!entry) return undefined;
  if (Date.now() > entry.expires) {
    store.delete(key);
    return undefined;
  }
  return entry.value;
}

export function setCached(key, value, maxAgeMs) {
  store.set(key, { value, expires: Date.now() + maxAgeMs });
}

export function invalidateCachePrefix(prefix) {
  for (const key of store.keys()) {
    if (key.startsWith(prefix)) store.delete(key);
  }
}

export function clearAllCache() {
  store.clear();
}
