import httpClient from "./httpClient.js";

/** Backend discriminator for cart lines (Swagger: itemType string). */
export const ITEM_TYPE_PRODUCT = "Product";
export const ITEM_TYPE_EQUIPMENT = "Equipment";

/**
 * GET /api/Cart — requires Bearer token.
 */
export async function fetchCart() {
  const { data } = await httpClient.get("/api/Cart");
  return data;
}

/**
 * POST /api/Cart/items — AddToCartRequest { itemType, itemId, quantity }
 */
export async function addCartItem({ itemType = ITEM_TYPE_PRODUCT, itemId, quantity }) {
  const { data } = await httpClient.post("/api/Cart/items", {
    itemType,
    itemId,
    quantity,
  });
  return data;
}

/**
 * PUT /api/Cart/items/{cartItemId}?quantity=
 */
export async function updateCartItemQuantity(cartItemId, quantity) {
  const { data } = await httpClient.put(`/api/Cart/items/${cartItemId}`, null, {
    params: { quantity },
  });
  return data;
}

/**
 * DELETE /api/Cart/items/{cartItemId}
 */
export async function removeCartItem(cartItemId) {
  const { data } = await httpClient.delete(`/api/Cart/items/${cartItemId}`);
  return data;
}
