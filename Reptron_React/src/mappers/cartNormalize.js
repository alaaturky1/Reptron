import { mapImageByEntity } from "../services/imageMap.js";
import { ITEM_TYPE_EQUIPMENT, ITEM_TYPE_PRODUCT } from "../api/cartApi.js";

function pickLines(raw) {
  if (!raw) return [];
  if (Array.isArray(raw)) return raw;
  const candidates = [raw.value, raw.items, raw.cartItems, raw.lines, raw.data?.items];
  for (const c of candidates) {
    if (Array.isArray(c)) return c;
  }
  return [];
}

/**
 * Map server cart payload into UI cart rows (aligned with cartContext shape).
 */
export function normalizeServerCartLines(serverPayload) {
  const lines = pickLines(serverPayload);
  return lines.map((line) => {
    const productId =
      line.productId ??
      line.itemId ??
      line.product?.id ??
      line.id;

    const cartItemId =
      line.cartItemId ??
      line.cartLineId ??
      line.lineId ??
      line.id;

    const name =
      line.productName ??
      line.name ??
      line.product?.name ??
      line.title ??
      "Item";

    const price = Number(
      line.unitPrice ??
        line.price ??
        line.product?.price ??
        line.amount ??
        0
    );

    const quantity = Math.max(1, Number(line.quantity ?? 1));

    const itemType = line.itemType ?? line.type ?? ITEM_TYPE_PRODUCT;
    const imagePrefix =
      String(itemType).toLowerCase() === String(ITEM_TYPE_EQUIPMENT).toLowerCase()
        ? "equipments"
        : "products";

    const img = mapImageByEntity(
      { ...line, id: productId, name, imageUrl: line.imageUrl ?? line.product?.imageUrl },
      imagePrefix
    );

    return {
      id: productId,
      cartItemId,
      itemType,
      name,
      price,
      quantity,
      img,
      image: img,
      synced: true,
      /** Used for quantity controls + remove matching guest rows */
      lineKey: `${productId}-${itemType}`,
    };
  });
}
