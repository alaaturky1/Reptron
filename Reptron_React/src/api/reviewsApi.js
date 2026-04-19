import httpClient from "./httpClient.js";
import { unwrapListPayload } from "./responseUtils.js";

/**
 * GET /api/Reviews/products/{productId}
 */
export async function fetchProductReviews(productId) {
  const { data } = await httpClient.get(`/api/Reviews/products/${productId}`);
  return unwrapListPayload(data);
}

/**
 * POST /api/Reviews/products/{productId} — requires auth (Swagger).
 */
export async function createProductReview(productId, body) {
  const { data } = await httpClient.post(`/api/Reviews/products/${productId}`, body);
  return data;
}
