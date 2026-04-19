import httpClient from "./httpClient.js";
import { unwrapListPayload } from "./responseUtils.js";

/**
 * POST /api/Orders — CreateOrderRequest { shippingAddress: string }
 */
export async function createOrder({ shippingAddress }) {
  const { data } = await httpClient.post("/api/Orders", { shippingAddress });
  return data;
}

/**
 * GET /api/Orders
 */
export async function fetchOrders() {
  const { data } = await httpClient.get("/api/Orders");
  return unwrapListPayload(data);
}

/**
 * GET /api/Orders/{orderId}
 */
export async function fetchOrderById(orderId) {
  const { data } = await httpClient.get(`/api/Orders/${orderId}`);
  return data;
}
