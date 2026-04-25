/**
 * Central place for Vite env vars (import.meta.env is inlined at build time).
 */
const raw = import.meta.env.VITE_API_BASE_URL?.trim();

/** Base URL for PowerFuel API (no trailing slash). */
export const API_BASE_URL = raw || "http://power-fuelgym00.runasp.net";
