import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import styles from "./Profile.module.css";
import { getOrders } from "../../services/storeService";
import { mapImageByEntity } from "../../services/imageMap";
import { LOCAL_AUTH_PASSWORD_KEY } from "../../services/apiClient";

const USER_PROFILE_KEY = "userProfile";

/** Same rule as Register: uppercase first, then 5–9 lowercase letters or digits. */
const NEW_PASSWORD_REGEX = /^[A-Z][a-z0-9]{5,9}$/;

function unwrapOrders(data) {
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.value)) return data.value;
    if (Array.isArray(data?.data)) return data.data;
    return [];
}

/** When API stores shipping as one string (matches checkout format). */
function parseShippingBlob(str) {
    if (typeof str !== "string") return undefined;
    const segments = str.split("|").map((s) => s.trim());
    return {
        name: segments[0] ?? "",
        email: segments[1] ?? "",
        address: segments[2] ?? str,
        city: "",
        postalCode: "",
        country: "",
    };
}

export default function Profile() {
    const [orders, setOrders] = useState([]);
    const [profile, setProfile] = useState({ name: "", email: "", image: "" });
    const [editMode, setEditMode] = useState(false);
    const [editableName, setEditableName] = useState("");
    const [hasLocalPassword, setHasLocalPassword] = useState(() => !!localStorage.getItem(LOCAL_AUTH_PASSWORD_KEY));

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [pwdErrors, setPwdErrors] = useState({});
    const [passwordPanelOpen, setPasswordPanelOpen] = useState(false);

    function closePasswordPanel() {
        setPasswordPanelOpen(false);
        setCurrentPassword("");
        setNewPassword("");
        setConfirmPassword("");
        setPwdErrors({});
    }

    useEffect(() => {
        try {
            const storedProfile = JSON.parse(localStorage.getItem(USER_PROFILE_KEY) || "{}");
            setProfile({
                name: storedProfile.name || "",
                email: storedProfile.email || "",
                image: storedProfile.image || "",
            });
            setEditableName(storedProfile.name || "");
        } catch {
            setProfile({ name: "", email: "", image: "" });
            setEditableName("");
        }

        setHasLocalPassword(!!localStorage.getItem(LOCAL_AUTH_PASSWORD_KEY));

        getOrders()
            .then((data) => {
                const parsed = Array.isArray(data) ? data : unwrapOrders(data);
                const normalized = parsed.map((order) => ({
                    id: order.id ?? order.orderId ?? order.Id ?? Date.now(),
                    date: order.date ?? order.createdAt ?? order.orderDate ?? new Date().toISOString(),
                    total: (() => {
                        const t = Number(order.total ?? order.grandTotal ?? order.amount ?? 0);
                        return Number.isFinite(t) ? t : 0;
                    })(),
                    items: (order.items ?? order.orderItems ?? order.lines ?? []).map((item) => ({
                        id: item.id ?? item.productId ?? item.itemId,
                        img: mapImageByEntity(item, "products"),
                        name: item.name ?? item.productName ?? "Item",
                        price: Number(item.price ?? item.unitPrice ?? 0),
                        quantity: Number(item.quantity ?? 1),
                    })),
                    shippingAddress: typeof order.shippingAddress === "string"
                        ? parseShippingBlob(order.shippingAddress)
                        : order.shippingAddress,
                }));
                setOrders(normalized.reverse());
            })
            .catch(() => {
                let savedOrders = [];
                try {
                    const raw = localStorage.getItem("purchases");
                    if (raw) {
                        const parsed = JSON.parse(raw);
                        savedOrders = Array.isArray(parsed) ? parsed : [];
                    }
                } catch {
                    savedOrders = [];
                }
                const normalized = savedOrders.map((order) => ({
                    id: order.id ?? order.orderId ?? order.Id ?? Date.now(),
                    date: order.date ?? order.createdAt ?? order.orderDate ?? new Date().toISOString(),
                    total: Number(order.total ?? order.grandTotal ?? order.amount ?? 0) || 0,
                    items: Array.isArray(order.items)
                        ? order.items.map((item, idx) => ({
                            id: item.id ?? item.productId ?? item.itemId ?? idx,
                            img: item.img ?? item.image ?? "",
                            name: item.name ?? item.productName ?? "Item",
                            price: Number(item.price ?? item.unitPrice ?? 0) || 0,
                            quantity: Number(item.quantity ?? 1) || 1,
                        }))
                        : [],
                    shippingAddress: order.shippingAddress,
                }));
                setOrders(normalized.reverse());
            });
    }, []);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "2-digit",
            minute: "2-digit",
        });
    };

    function saveProfile() {
        const updatedProfile = {
            ...profile,
            name: editableName.trim() || profile.name,
        };
        setProfile(updatedProfile);
        localStorage.setItem(USER_PROFILE_KEY, JSON.stringify(updatedProfile));
        setEditMode(false);
    }

    function handleProfileImageChange(event) {
        const file = event.target.files?.[0];
        if (!file) return;
        const reader = new FileReader();
        reader.onloadend = () => {
            setProfile((prev) => {
                const updatedProfile = { ...prev, image: reader.result };
                localStorage.setItem(USER_PROFILE_KEY, JSON.stringify(updatedProfile));
                return updatedProfile;
            });
        };
        reader.readAsDataURL(file);
    }

    function validateAndChangePassword(event) {
        event.preventDefault();
        const stored = localStorage.getItem(LOCAL_AUTH_PASSWORD_KEY);
        const next = {};

        if (!stored) {
            toast.error("Password change is only available after signing in with email and password.");
            return;
        }

        if (!currentPassword) {
            next.current = "Current password is required.";
        } else if (currentPassword !== stored) {
            next.current = "Current password is incorrect.";
        }

        if (!newPassword) {
            next.new = "New password is required.";
        } else if (!NEW_PASSWORD_REGEX.test(newPassword)) {
            next.new = "Must start with one uppercase letter, then 5–9 lowercase letters or digits (same as registration).";
        } else if (newPassword === stored) {
            next.new = "New password must be different from your current password.";
        }

        if (!confirmPassword) {
            next.confirm = "Confirm your new password.";
        } else if (confirmPassword !== newPassword) {
            next.confirm = "Passwords do not match.";
        }

        setPwdErrors(next);
        if (Object.keys(next).length > 0) return;

        localStorage.setItem(LOCAL_AUTH_PASSWORD_KEY, newPassword);
        setHasLocalPassword(true);
        closePasswordPanel();
        toast.success("Password updated locally. Use the new password next time you sign in.");
    }

    return (
        <div className={styles.profilePage}>
            <div className={styles.glowEffect}></div>
            <div className="container">
                <div className={styles.header}>
                    <h1 className={styles.title}>Profile</h1>
                    <p className={styles.subtitle}>View your order history and details</p>
                </div>
                <div className={styles.profileCard}>
                    <div className={styles.profileImageWrapper}>
                        {profile.image ? (
                            <img src={profile.image} alt="Profile" className={styles.profileImage} />
                        ) : (
                            <i className={`fas fa-user ${styles.profileImagePlaceholder}`}></i>
                        )}
                    </div>
                    <div className={styles.profileInfo}>
                        {editMode ? (
                            <input
                                type="text"
                                value={editableName}
                                onChange={(event) => setEditableName(event.target.value)}
                                className={styles.profileInput}
                                placeholder="Enter your name"
                            />
                        ) : (
                            <h3 className={styles.profileName}>{profile.name || "No name available"}</h3>
                        )}
                        <p className={styles.profileEmail}>{profile.email || "No email available"}</p>
                        {editMode ? (
                            <div className={styles.profileActions}>
                                <label className={styles.profileActionButton}>
                                    <i className="fas fa-images"></i> Gallery
                                    <input type="file" accept="image/*" onChange={handleProfileImageChange} hidden />
                                </label>
                                <button type="button" className={styles.profileActionButton} onClick={saveProfile}>
                                    <i className="fas fa-save"></i> Save
                                </button>
                            </div>
                        ) : (
                            <button type="button" className={styles.editProfileButton} onClick={() => setEditMode(true)}>
                                Edit Profile
                            </button>
                        )}

                        {hasLocalPassword ? (
                            <div className={styles.passwordSection}>
                                <button
                                    type="button"
                                    className={styles.passwordToggleButton}
                                    onClick={() => (passwordPanelOpen ? closePasswordPanel() : setPasswordPanelOpen(true))}
                                    aria-expanded={passwordPanelOpen}
                                >
                                    <i className="fas fa-key"></i>
                                    {passwordPanelOpen ? "Hide password form" : "Change password"}
                                    <i className={`fas fa-chevron-${passwordPanelOpen ? "up" : "down"}`}></i>
                                </button>
                                {passwordPanelOpen && (
                                    <form className={styles.passwordForm} onSubmit={validateAndChangePassword} noValidate>
                                        <p className={styles.passwordHint}>
                                            Rules: one uppercase letter at the start, then 5–9 lowercase letters or digits (matches registration). This updates the password stored in this browser for verification only; sign in again with your new password if your API supports it.
                                        </p>
                                        <div className={styles.passwordField}>
                                            <label className={styles.passwordLabel} htmlFor="profile-current-password">Current password</label>
                                            <input
                                                id="profile-current-password"
                                                type="password"
                                                autoComplete="current-password"
                                                className={styles.passwordInput}
                                                value={currentPassword}
                                                onChange={(e) => setCurrentPassword(e.target.value)}
                                            />
                                            {pwdErrors.current && <div className={styles.passwordError}>{pwdErrors.current}</div>}
                                        </div>
                                        <div className={styles.passwordField}>
                                            <label className={styles.passwordLabel} htmlFor="profile-new-password">New password</label>
                                            <input
                                                id="profile-new-password"
                                                type="password"
                                                autoComplete="new-password"
                                                className={styles.passwordInput}
                                                value={newPassword}
                                                onChange={(e) => setNewPassword(e.target.value)}
                                            />
                                            {pwdErrors.new && <div className={styles.passwordError}>{pwdErrors.new}</div>}
                                        </div>
                                        <div className={styles.passwordField}>
                                            <label className={styles.passwordLabel} htmlFor="profile-confirm-password">Confirm new password</label>
                                            <input
                                                id="profile-confirm-password"
                                                type="password"
                                                autoComplete="new-password"
                                                className={styles.passwordInput}
                                                value={confirmPassword}
                                                onChange={(e) => setConfirmPassword(e.target.value)}
                                            />
                                            {pwdErrors.confirm && <div className={styles.passwordError}>{pwdErrors.confirm}</div>}
                                        </div>
                                        <div className={styles.passwordFormActions}>
                                            <button type="button" className={styles.passwordCancel} onClick={closePasswordPanel}>
                                                Cancel
                                            </button>
                                            <button type="submit" className={styles.passwordSubmit}>
                                                Update password
                                            </button>
                                        </div>
                                    </form>
                                )}
                            </div>
                        ) : (
                            <div className={`${styles.passwordSection} ${styles.passwordDisabledNote}`}>
                                <h4 className={styles.passwordSectionTitle}>
                                    <i className="fas fa-key"></i> Change password
                                </h4>
                                <p>
                                    Available after you sign in with email and password (not social login). Sign out and sign in once to enable this section.
                                </p>
                            </div>
                        )}
                    </div>
                </div>
                {orders.length === 0 ? (
                    <div className={styles.emptyState}>
                        <i className={`fas fa-shopping-bag ${styles.emptyIcon}`}></i>
                        <h2 className={styles.emptyTitle}>No purchases yet</h2>
                        <p className={styles.emptyText}>
                            You haven't made any purchases yet. Start shopping to see your orders here!
                        </p>
                        <Link to="/" className={styles.shopButton}>
                            <i className="fas fa-store"></i>
                            Start Shopping
                        </Link>
                    </div>
                ) : (
                    orders.map((order) => (
                        <div key={order.id} className={styles.orderCard}>
                            <div className={styles.orderHeader}>
                                <div className={styles.orderInfo}>
                                    <div className={styles.orderDate}>
                                        <i className="fas fa-calendar-alt"></i>
                                        {formatDate(order.date)}
                                    </div>
                                </div>
                                <div className={styles.orderTotal}>
                                    ${(Number.isFinite(order.total) ? order.total : 0).toFixed(2)}
                                </div>
                            </div>

                            <div className={styles.orderBody}>
                                <h4 className={styles.shippingTitle}>
                                    <i className="fas fa-box"></i>
                                    Order Items
                                </h4>

                                <div className={styles.orderItems}>
                                    {order.items.map((item, itemIdx) => (
                                        <div key={item.id ?? itemIdx} className={styles.orderItem}>
                                            <img src={item.img} alt={item.name} className={styles.itemImage} />
                                            <div className={styles.itemDetails}>
                                                <div className={styles.itemName}>{item.name}</div>
                                                <div className={styles.itemPrice}>
                                                    Price: ${item.price.toFixed(2)} each
                                                </div>
                                                <div className={styles.itemQuantity}>
                                                    Quantity: {item.quantity}
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

                                {order.shippingAddress && (
                                    <div className={styles.shippingInfo}>
                                        <h4 className={styles.shippingTitle}>
                                            <i className="fas fa-truck"></i>
                                            Shipping Information
                                        </h4>
                                        <div className={styles.shippingDetails}>
                                            <div className={styles.shippingField}>
                                                <strong>Name:</strong> {order.shippingAddress.name}
                                            </div>
                                            <div className={styles.shippingField}>
                                                <strong>Address:</strong> {order.shippingAddress.address}
                                            </div>
                                            <div className={styles.shippingField}>
                                                <strong>City:</strong> {order.shippingAddress.city}
                                            </div>
                                            <div className={styles.shippingField}>
                                                <strong>Postal Code:</strong> {order.shippingAddress.postalCode}
                                            </div>
                                            <div className={styles.shippingField}>
                                                <strong>Country:</strong> {order.shippingAddress.country}
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
