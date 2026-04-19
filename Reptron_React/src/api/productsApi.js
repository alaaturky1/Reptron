import httpClient from "./httpClient.js";
import { unwrapListPayload } from "./responseUtils.js";
import { getCached, setCached } from "./simpleCache.js";

const LIST_TTL_MS = 60_000;
const DETAIL_TTL_MS = 60_000;

/**
 * GET /api/Products — optional query: category, search
 */
export async function fetchProducts(params = {}, { useCache = true } = {}) {
  const category = params.category ?? "";
  const search = params.search ?? "";
  const cacheKey = `products:list:${category}:${search}`;

  if (useCache) {
    const hit = getCached(cacheKey);
    if (hit !== undefined) return hit;
  }

  const { data } = await httpClient.get("/api/Products", {
    params: { category: category || undefined, search: search || undefined },
  });
  const list = unwrapListPayload(data);
  if (useCache) setCached(cacheKey, list, LIST_TTL_MS);
  return list;
}

/**
 * GET /api/Products/{id}
 */
export async function fetchProductById(id, { useCache = true } = {}) {
  const cacheKey = `products:detail:${id}`;
  if (useCache) {
    const hit = getCached(cacheKey);
    if (hit !== undefined) return hit;
  }

  const { data } = await httpClient.get(`/api/Products/${id}`);
  if (useCache) setCached(cacheKey, data, DETAIL_TTL_MS);
  return data;
}

/**
 * GET /api/Products/best-sellers?count=
 */
export async function fetchBestSellers(count = 8, { useCache = true } = {}) {
  const cacheKey = `products:best:${count}`;
  if (useCache) {
    const hit = getCached(cacheKey);
    if (hit !== undefined) return hit;
  }

  const { data } = await httpClient.get("/api/Products/best-sellers", {
    params: { count },
  });
  const list = unwrapListPayload(data);
  if (useCache) setCached(cacheKey, list, LIST_TTL_MS);
  return list;
}
