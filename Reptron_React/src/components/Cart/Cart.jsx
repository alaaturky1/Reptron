import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { cartContext } from "../../context/cartContext";
import { ITEM_TYPE_PRODUCT } from "../../api/cartApi.js";
import toast, { Toaster } from "react-hot-toast";
import styles from './Cart.module.css';

export default function CartPage() {
  const { cart, addToCart, decreaseQuantity, removeFromCart, clearCart } = useContext(cartContext);
  const navigate = useNavigate();

  const grandTotal = cart.reduce((acc, item) => acc + item.price * item.quantity, 0);
  const itemsCount = cart.reduce((acc, item) => acc + item.quantity, 0);

  const handleRemove = async (id, name, itemType = ITEM_TYPE_PRODUCT) => {
    try {
      await removeFromCart(id, itemType);
    } catch {
      return;
    }
    toast.error(`${name} removed from cart`, { 
        style: {
            background: "#1e293b",
            color: "#ff6b6b",
            border: "1px solid #ff6b6b",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
        }
    });
  };

  const handleDecrease = async (id, name, itemType = ITEM_TYPE_PRODUCT) => {
    try {
      await decreaseQuantity(id, itemType);
    } catch {
      return;
    }
    toast(`${name} quantity decreased`, { 
        style: {
            background: "#1e293b",
            color: "#00e5ff",
            border: "1px solid #00e5ff",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
        },
        icon: "➖" 
    });
  };

  const handleIncrease = async (item) => {
    try {
      await addToCart(item, 1, item.itemType ?? ITEM_TYPE_PRODUCT);
    } catch {
      return;
    }
    toast.success(`${item.name} quantity increased`, { 
        style: {
            background: "#1e293b",
            color: "#00e5ff",
            border: "1px solid #00e5ff",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
        }
    });
  };

  const handleClear = async () => {
    try {
      await clearCart();
    } catch {
      return;
    }
    toast.error(`Cart cleared!`, { 
        style: {
            background: "#1e293b",
            color: "#ff6b6b",
            border: "1px solid #ff6b6b",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
        }
    });
  };

  const handleCheckout = () => {
    if (cart.length === 0) {
      toast.error("Your cart is empty!", { 
        style: {
            background: "#1e293b",
            color: "#ff6b6b",
            border: "1px solid #ff6b6b",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
        }
      });
      return;
    }
    navigate("/checkout");
  };

  return (
    <div className={styles.cartContainer}>
      <div className={styles.glowEffect}></div>
      <Toaster toastOptions={{
            duration: 3000,
            style: {
                background: "#1e293b",
                color: "#00e5ff",
                border: "1px solid #00e5ff",
                borderRadius: "12px",
                padding: "16px",
                fontWeight: "bold",
            },}}/>

      <div className="container">
        <div className={styles.header}>
          <h1 className={styles.title}>Your Cart</h1>
          <p className={styles.subtitle}>Review and manage your items</p>
        </div>

        {cart.length === 0 ? (
          <div className={styles.emptyState}>
            <i className={`fas fa-shopping-cart ${styles.emptyIcon}`}></i>
            <h2 className={styles.emptyTitle}>Your cart is empty</h2>
            <p className={styles.emptyText}>
              Looks like you haven't added any items to your cart yet. Start shopping to fill it up!
            </p>
            <button className={styles.shopButton} onClick={() => navigate("/")}>
              <i className="fas fa-store"></i>
              Start Shopping
            </button>
          </div>
        ) : (
          <>
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <h3 className={styles.cardTitle}>
                  <i className="fas fa-shopping-cart"></i>
                  Cart Items
                  <span className={styles.cartBadge}>
                    {itemsCount} {itemsCount > 1 ? "items" : "item"}
                  </span>
                </h3>
              </div>
              <div className={styles.cardBody}>
                {cart.map(item => (
                  <div key={item.lineKey ?? `${item.id}-${item.itemType ?? ITEM_TYPE_PRODUCT}`} className={styles.cartItem}>
                    <img src={item.img || item.image} alt={item.name} className={styles.itemImage}/>
                    <div className={styles.itemDetails}>
                      <div className={styles.itemName}>{item.name}</div>
                      <div className={styles.itemPrice}>
                        Price: ${item.price.toFixed(2)} each
                      </div>
                      <div className={styles.itemActions}>
                        <div className={styles.quantityControls}>
                          <button className={styles.quantityButton} onClick={() => handleDecrease(item.id, item.name, item.itemType ?? ITEM_TYPE_PRODUCT)}>
                            <i className="fas fa-minus"></i>
                          </button>
                          <input type="number" min="1" value={item.quantity} className={styles.quantityInput} readOnly/>
                          <button className={styles.quantityButton} onClick={() => handleIncrease(item)}>
                            <i className="fas fa-plus"></i>
                          </button>
                        </div>
                        <button className={styles.removeButton} onClick={() => handleRemove(item.id, item.name, item.itemType ?? ITEM_TYPE_PRODUCT)}>
                          <i className="fas fa-trash"></i>
                          Remove
                        </button>
                      </div>
                    </div>
                    <div className={styles.itemTotal}>
                      <div className={styles.itemTotalAmount}>
                        ${(item.price * item.quantity).toFixed(2)}
                      </div>
                      <div className={styles.itemTotalLabel}>
                        Item Total
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <h3 className={styles.cardTitle}>
                  <i className="fas fa-receipt"></i>
                  Order Summary
                </h3>
              </div>
              <div className={styles.cardBody}>
                <div className={styles.summaryList}>
                  <div className={styles.summaryItem}>
                    <span>Subtotal</span>
                    <span>${grandTotal.toFixed(2)}</span>
                  </div>
                  <div className={styles.summaryItem}>
                    <span>Shipping</span>
                    <span>Free</span>
                  </div>
                  <div className={styles.summaryItem}>
                    <span>Tax</span>
                    <span>Included</span>
                  </div>
                </div>

                <div className={styles.summaryTotal}>
                  <span className={styles.totalLabel}>Total Amount</span>
                  <span className={styles.totalAmount}>${grandTotal.toFixed(2)}</span>
                </div>

                <div className={styles.actionButtons}>
                  <button className={styles.clearButton} onClick={handleClear}>
                    <i className="fas fa-trash-alt"></i>
                    Clear Cart
                  </button>
                  <button className={styles.checkoutButton} onClick={handleCheckout}>
                    <i className="fas fa-lock"></i>
                    Proceed to Checkout
                  </button>
                </div>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}