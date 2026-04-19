import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { cartContext } from "../../context/cartContext";
import toast, { Toaster } from "react-hot-toast";
import { purchaseContext } from "../../context/purchasesContext";
import styles from './CheckOut.module.css';
import { submitOrder } from "../../services/storeService";
import { getApiErrorMessage } from "../../api/responseUtils.js";

export default function CheckoutPage() {
    const { cart, clearCart, ensureServerCartSynced } = useContext(cartContext);
    const navigate = useNavigate();
    const { addPurchase } = useContext(purchaseContext);

    const [form, setForm] = useState({
        name: "",
        email: "",
        address: "",
        city: "",
        postalCode: "",
        country: "",
        cardNumber: "",
        cardName: "",
        expiry: "",
        cvv: "",
    });

    const [isLoading, setIsLoading] = useState(false);

    const grandTotal = cart.reduce((acc, item) => acc + item.price * item.quantity, 0);

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "cardNumber") {
            const formatted = value.replace(/\D/g, "").substring(0, 16).replace(/(.{4})/g, "$1 ").trim();
            setForm({ ...form, [name]: formatted });
        }
        else if (name === "expiry") {
            let formatted = value.replace(/\D/g, "").substring(0, 4);
            if (formatted.length > 2) formatted = formatted.substring(0, 2) + "/" + formatted.substring(2, 4);
            setForm({ ...form, [name]: formatted });
        } else {
            setForm({ ...form, [name]: value });
        }
    };

    const handlePlaceOrder = async () => {
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

        const requiredFields = [
            "name",
            "email",
            "address",
            "city",
            "postalCode",
            "country",
            "cardNumber",
            "cardName",
            "expiry",
            "cvv"
        ];

        for (let field of requiredFields) {
            if (!form[field]) {
                toast.error("Please fill all the fields!", {
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
        }

        setIsLoading(true);
        try {
            const orderItemsSnapshot = [...cart];
            /** API expects a single shipping string (Swagger: CreateOrderRequest.shippingAddress). */
            const shippingAddress = [
                `${form.name}`,
                `${form.email}`,
                `${form.address}, ${form.city} ${form.postalCode}, ${form.country}`,
            ].join(" | ");

            await ensureServerCartSynced();
            const response = await submitOrder(shippingAddress);
            toast.success(
                (typeof response === "object" && response?.message) || "Order placed successfully!",
                {
                style: {
                    background: "#1e293b",
                    color: "#00e5ff",
                    border: "1px solid #00e5ff",
                    padding: "16px",
                    borderRadius: "12px",
                    fontWeight: "bold",
                }
            });
            
            await clearCart();
            const previousOrders = JSON.parse(localStorage.getItem("purchases")) || [];

            const newOrder = {
                id: response?.id ?? response?.orderId ?? Date.now(),
                date: new Date().toLocaleString(),
                items: orderItemsSnapshot,
                total: grandTotal,
                shippingAddress: {
                    name: form.name,
                    address: form.address,
                    city: form.city,
                    postalCode: form.postalCode,
                    country: form.country
                }
            };
            
            localStorage.setItem("purchases", JSON.stringify([...previousOrders, newOrder]));
            addPurchase(newOrder);

            setForm({
                name: "",
                email: "",
                address: "",
                city: "",
                postalCode: "",
                country: "",
                cardNumber: "",
                cardName: "",
                expiry: "",
                cvv: "",
            });
            setTimeout(() => navigate("/profile"), 2000);
        } catch (error) {
            toast.error(getApiErrorMessage(error), {
                style: {
                    background: "#1e293b",
                    color: "#ff6b6b",
                    border: "1px solid #ff6b6b",
                    padding: "16px",
                    borderRadius: "12px",
                    fontWeight: "bold",
                }
            });
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <div className={styles.checkoutContainer}>
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
                    },
                }}/>

            <div className="container">
                <div className={styles.header}>
                    <h1 className={styles.title}>Checkout</h1>
                    <p className={styles.subtitle}>Complete your purchase securely</p>
                </div>

                <div className={styles.checkoutGrid}>
                    <div className={styles.card}>
                        <div className={styles.cardHeader}>
                            <h3 className={styles.cardTitle}>
                                <i className="fas fa-shopping-cart"></i>
                                Your Cart
                            </h3>
                        </div>
                        <div className={styles.cardBody}>
                            {cart.length === 0 ? (
                                <div className={styles.cartEmpty}>
                                    <i className="fas fa-shopping-cart"></i>
                                    <p>Your cart is empty</p>
                                </div>
                            ) : (
                                <>
                                    {cart.map(item => (
                                        <div key={item.id} className={styles.cartItem}>
                                            <div className={styles.itemInfo}>
                                                <div className={styles.itemName}>{item.name}</div>
                                                <div className={styles.itemQuantity}>Quantity: {item.quantity}</div>
                                            </div>
                                            <div className={styles.itemPrice}>
                                                ${(item.price * item.quantity).toFixed(2)}
                                            </div>
                                        </div>
                                    ))}
                                    <div className={styles.totalSection}>
                                        <div className={styles.totalRow}>
                                            <span className={styles.totalLabel}>Grand Total:</span>
                                            <span className={styles.totalAmount}>${grandTotal.toFixed(2)}</span>
                                        </div>
                                    </div>
                                </>
                            )}
                        </div>
                    </div>

                    <div className={styles.card}>
                        <div className={styles.cardHeader}>
                            <h3 className={styles.cardTitle}>
                                <i className="fas fa-user-circle"></i>
                                Billing Information
                            </h3>
                        </div>
                        <div className={styles.cardBody}>
                            {["name", "email", "address", "city", "postalCode", "country"].map(field => (
                                <div key={field} className={styles.formGroup}>
                                    <label className={styles.formLabel}>
                                        {field.charAt(0).toUpperCase() + field.slice(1).replace("Code", " Code")}
                                    </label>
                                    <div className={styles.cardInputContainer}>
                                        <input type={field === "email" ? "email" : "text"} name={field} value={form[field]} onChange={handleChange} className={styles.formInput} placeholder={`Enter your ${field.replace("Code", " code")}`} disabled={isLoading}/>
                                        <i className={`fas fa-${field === 'email' ? 'envelope' : field === 'address' ? 'map-marker-alt' : 'user'} ${styles.inputIcon}`}></i>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className={styles.card}>
                        <div className={styles.cardHeader}>
                            <h3 className={styles.cardTitle}>
                                <i className="fas fa-credit-card"></i>
                                Payment Details
                            </h3>
                        </div>
                        <div className={styles.cardBody}>
                            <div className={styles.formGroup}>
                                <label className={styles.formLabel}>Card Number</label>
                                <div className={styles.cardInputContainer}>
                                    <input type="text" name="cardNumber" value={form.cardNumber} onChange={handleChange} className={styles.formInput} placeholder="1234 5678 9012 3456" disabled={isLoading}/>
                                    <i className="fas fa-credit-card" style={{position: 'absolute',left: '1rem',top: '50%',transform: 'translateY(-50%)',color: '#94a3b8',fontSize: '1.2rem'}}></i>
                                </div>
                            </div>

                            <div className={styles.formGroup}>
                                <label className={styles.formLabel}>Name on Card</label>
                                <div className={styles.cardInputContainer}>
                                    <input type="text" name="cardName" value={form.cardName} onChange={handleChange} className={styles.formInput} placeholder="JOHN DOE" disabled={isLoading}/>
                                    <i className={`fas fa-user ${styles.inputIcon}`}></i>
                                </div>
                            </div>

                            <div className={styles.cardRow}>
                                <div className={styles.formGroup}>
                                    <label className={styles.formLabel}>Expiry Date</label>
                                    <div className={styles.cardInputContainer}>
                                        <input type="text" name="expiry" value={form.expiry} onChange={handleChange} className={styles.formInput} placeholder="MM/YY" disabled={isLoading}/>
                                        <i className={`fas fa-calendar-alt ${styles.inputIcon}`}></i>
                                    </div>
                                </div>
                                <div className={styles.formGroup}>
                                    <label className={styles.formLabel}>CVV</label>
                                    <div className={styles.cardInputContainer}>
                                        <input type="text" name="cvv" value={form.cvv} onChange={handleChange} className={styles.formInput} placeholder="123" maxLength="4" disabled={isLoading}/>
                                        <i className={`fas fa-lock ${styles.inputIcon}`}></i>
                                    </div>
                                </div>
                            </div>

                            <button className={styles.submitButton}  onClick={handlePlaceOrder} disabled={isLoading || cart.length === 0}>
                                {isLoading ? (
                                    <>
                                        <i className={`fas fa-spinner ${styles.buttonIcon} ${styles.loading}`}></i>
                                        Processing...
                                    </>
                                ) : (
                                    <>
                                        <i className={`fas fa-shopping-cart ${styles.buttonIcon}`}></i>
                                        Place Order - ${grandTotal.toFixed(2)}
                                    </>
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}