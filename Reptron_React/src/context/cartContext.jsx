import { createContext, useCallback, useContext, useEffect, useState } from "react";
import * as cartApi from "../api/cartApi.js";
import { normalizeServerCartLines } from "../mappers/cartNormalize.js";
import { getApiErrorMessage } from "../api/responseUtils.js";
import { userContext } from "./userContext.jsx";

export const cartContext = createContext();

const GUEST_CART_KEY = "supplementStore_guestCart";

function loadGuestCart() {
  try {
    const raw = localStorage.getItem(GUEST_CART_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return [];
    return parsed.map((line) => {
      const itemType = line.itemType ?? cartApi.ITEM_TYPE_PRODUCT;
      return {
        ...line,
        itemType,
        lineKey: line.lineKey ?? `${line.id}-${itemType}`,
      };
    });
  } catch {
    return [];
  }
}

function saveGuestCart(items) {
  try {
    localStorage.setItem(GUEST_CART_KEY, JSON.stringify(items));
  } catch {
    /* ignore quota */
  }
}

export function CartContextProvider({ children }) {
  const { isLogin } = useContext(userContext);
  const [cart, setCart] = useState([]);
  const [cartLoading, setCartLoading] = useState(false);
  const [cartError, setCartError] = useState(null);

  const refreshServerCart = useCallback(async () => {
    if (!isLogin) return;
    setCartLoading(true);
    setCartError(null);
    try {
      const raw = await cartApi.fetchCart();
      const next = normalizeServerCartLines(raw);
      setCart(next);
    } catch (e) {
      setCartError(getApiErrorMessage(e));
    } finally {
      setCartLoading(false);
    }
  }, [isLogin]);

  /** Push guest lines to API after login, then reload server cart. */
  const mergeGuestCartToServer = useCallback(
    async (guestLines) => {
      if (!guestLines.length) return;
      for (const line of guestLines) {
        try {
          await cartApi.addCartItem({
            itemType: line.itemType ?? cartApi.ITEM_TYPE_PRODUCT,
            itemId: line.id,
            quantity: line.quantity,
          });
        } catch (e) {
          console.warn("mergeGuestCartToServer item failed:", getApiErrorMessage(e));
        }
      }
      saveGuestCart([]);
      await refreshServerCart();
    },
    [refreshServerCart]
  );

  useEffect(() => {
    if (!isLogin) {
      setCart(loadGuestCart());
      return;
    }

    const guest = loadGuestCart();
    if (guest.length) {
      mergeGuestCartToServer(guest);
    } else {
      refreshServerCart();
    }
  }, [isLogin, mergeGuestCartToServer, refreshServerCart]);

  const addToCart = async (product, quantityDelta = 1, itemType = cartApi.ITEM_TYPE_PRODUCT) => {
    const qty = Math.max(1, Number(quantityDelta) || 1);

    if (!isLogin) {
      setCart((prev) => {
        const exists = prev.find((item) => item.id === product.id && item.itemType === itemType);
        let next;
        if (exists) {
          next = prev.map((item) =>
            item.id === product.id && item.itemType === itemType
              ? { ...item, quantity: item.quantity + qty }
              : item
          );
        } else {
          next = [
            ...prev,
            {
              ...product,
              quantity: qty,
              synced: false,
              itemType,
              lineKey: `${product.id}-${itemType}`,
            },
          ];
        }
        saveGuestCart(next);
        return next;
      });
      return;
    }

    try {
      await cartApi.addCartItem({
        itemType,
        itemId: product.id,
        quantity: qty,
      });
      await refreshServerCart();
    } catch (e) {
      setCartError(getApiErrorMessage(e));
      throw e;
    }
  };

  const removeFromCart = async (productId, lineItemType = cartApi.ITEM_TYPE_PRODUCT) => {
    if (!isLogin) {
      setCart((prev) => {
        const next = prev.filter(
          (item) => !(item.id === productId && item.itemType === lineItemType)
        );
        saveGuestCart(next);
        return next;
      });
      return;
    }

    const line = cart.find((c) => c.id === productId && c.itemType === lineItemType);
    if (line?.cartItemId) {
      try {
        await cartApi.removeCartItem(line.cartItemId);
        await refreshServerCart();
      } catch (e) {
        setCartError(getApiErrorMessage(e));
        throw e;
      }
    } else {
      setCart((prev) => prev.filter((item) => item.id !== productId));
    }
  };

  const decreaseQuantity = async (productId, lineItemType = cartApi.ITEM_TYPE_PRODUCT) => {
    if (!isLogin) {
      setCart((prev) => {
        const next = prev
          .map((item) =>
            item.id === productId && item.itemType === lineItemType
              ? { ...item, quantity: item.quantity - 1 }
              : item
          )
          .filter((item) => item.quantity > 0);
        saveGuestCart(next);
        return next;
      });
      return;
    }

    const line = cart.find((c) => c.id === productId && c.itemType === lineItemType);
    if (!line?.cartItemId) return;

    const nextQty = line.quantity - 1;
    try {
      if (nextQty <= 0) {
        await cartApi.removeCartItem(line.cartItemId);
      } else {
        await cartApi.updateCartItemQuantity(line.cartItemId, nextQty);
      }
      await refreshServerCart();
    } catch (e) {
      setCartError(getApiErrorMessage(e));
      throw e;
    }
  };

  const clearCart = async () => {
    if (!isLogin) {
      setCart([]);
      saveGuestCart([]);
      return;
    }

    const lines = [...cart];
    setCartLoading(true);
    try {
      for (const line of lines) {
        if (line.cartItemId) {
          try {
            await cartApi.removeCartItem(line.cartItemId);
          } catch (e) {
            console.warn("clearCart remove:", getApiErrorMessage(e));
          }
        }
      }
      await refreshServerCart();
    } finally {
      setCartLoading(false);
    }
  };

  /**
   * Before placing an order, ensure server cart contains current lines (guest → server is handled on login).
   * Call from checkout when authenticated: pushes any unsynced rows (should be rare).
   */
  const ensureServerCartSynced = async () => {
    if (!isLogin) return;
    for (const line of cart) {
      if (line.cartItemId) continue;
      await cartApi.addCartItem({
        itemType: line.itemType ?? cartApi.ITEM_TYPE_PRODUCT,
        itemId: line.id,
        quantity: line.quantity,
      });
    }
    await refreshServerCart();
  };

  return (
    <cartContext.Provider
      value={{
        cart,
        cartLoading,
        cartError,
        setCartError,
        addToCart,
        removeFromCart,
        decreaseQuantity,
        clearCart,
        refreshServerCart,
        ensureServerCartSynced,
      }}
    >
      {children}
    </cartContext.Provider>
  );
}
