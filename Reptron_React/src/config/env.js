/**
 * Central place for Vite env vars (import.meta.env is inlined at build time).
 */
const raw = import.meta.env.VITE_API_BASE_URL?.trim();

/** Base URL for PowerFuel API (no trailing slash). */
export const API_BASE_URL = raw || "http://gym-management-0.runasp.net";

const aiRaw = import.meta.env.VITE_AI_API_BASE_URL?.trim();
const aiPrefixRaw = import.meta.env.VITE_AI_API_PREFIX?.trim();

/** Base URL for AI coach backend (falls back to main API base). */
export const AI_API_BASE_URL = aiRaw || API_BASE_URL;

/** Optional AI API prefix, ex: "/api/FitnessCoach" or "". */
export const AI_API_PREFIX = aiPrefixRaw ? aiPrefixRaw.replace(/\/+$/, "") : "";

/** Optional API key for AI backend protected endpoints. */
export const AI_API_KEY = import.meta.env.VITE_AI_API_KEY?.trim() || "";
