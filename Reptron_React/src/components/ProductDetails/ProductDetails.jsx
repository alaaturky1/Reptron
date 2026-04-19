import { useState, useEffect, useContext } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { cartContext } from "../../context/cartContext";
import toast, { Toaster } from "react-hot-toast";
import { getProductById, getProducts, getProductReviews } from "../../services/storeService";
import { getApiErrorMessage } from "../../api/responseUtils.js";
import styles from './ProductDetails.module.css';

export default function ProductPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { addToCart } = useContext(cartContext);

  const { product } = location.state || {};
  const [products, setProducts] = useState([]);
  const [selected, setSelected] = useState(product || null);
  const [main, setMain] = useState(product?.img || "");
  const [qty, setQty] = useState(1);
  const [tab, setTab] = useState("description");
  const [loadError, setLoadError] = useState(null);

  useEffect(() => {
    let cancelled = false;
    setLoadError(null);
    (async () => {
      try {
        const [list, apiProduct, reviews] = await Promise.all([
          getProducts(),
          getProductById(id),
          getProductReviews(id),
        ]);
        if (cancelled) return;
        setProducts(list);
        const base = product || apiProduct;
        const picked =
          reviews?.length > 0 ? { ...base, reviews } : base;
        setSelected(picked);
        setMain(picked?.img || "");
      } catch (e) {
        if (!cancelled) setLoadError(getApiErrorMessage(e));
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [id, product]);

  const related = products.filter((p) => selected && p.id !== selected.id).sort(() => 0.5 - Math.random()).slice(0, 4);

  const handleAddToCart = async () => {
    if (!selected) return;
    try {
      await addToCart(selected, qty);
    } catch {
      return;
    }
    setQty(1);
    toast.success(`${selected.name} added to cart successfully`, {
      duration: 3000,
      style: {
        background: "#1e293b",
        color: "#00e5ff",      
        border: "1px solid #00e5ff",
        padding: "16px",
        borderRadius: "12px",
        fontWeight: "bold",
      },
    });
  };

  const StarRating = ({ rating }) => {
    return (
      <div className={styles.starRating}>
        {[...Array(5)].map((_, index) => (
          <i key={index} className={`fas fa-star ${index < rating ? styles.star : 'text-secondary'}`}></i>
        ))}
      </div>
    );
  };

  return (
    <div className={styles.productContainer}>
      <div className={styles.glowEffect}></div>

      <Toaster toastOptions={{
          duration: 3000,
          style: {
            background: "#1e293b",
            color: "#00e5ff",      
            border: "1px solid #00e5ff",
            padding: "16px",
            borderRadius: "12px",
            fontWeight: "bold",
          },}}/>

      <div className="container">
        <button className={styles.backButton} onClick={() => navigate("/supplements")}>
          <i className="fas fa-arrow-left"></i>
          Back to Supplements
        </button>

        {loadError && (
          <div className="alert alert-danger my-3" role="alert">
            {loadError}
          </div>
        )}

        <div className={styles.productGrid}>
          <div className={styles.imageSection}>
            <img src={main} alt={selected?.name} className={styles.mainImage}/>
          </div>

          <div className={styles.infoSection}>
            <h1 className={styles.productTitle}>{selected?.name}</h1>
            <div className={styles.stockBadge}>In Stock</div>

            <div className={styles.priceSection}>
              <span className={styles.currentPrice}>${(selected?.price || 0).toFixed(2)}</span>
              {selected?.oldPrice && (
                <span className={styles.oldPrice}>${selected.oldPrice.toFixed(2)}</span>
              )}
            </div>

            <div className={styles.quantitySection}>
              <label className={styles.quantityLabel}>Quantity</label>
              <div className={styles.quantityControls}>
                <button className={styles.quantityButton} onClick={() => setQty(q => Math.max(1, q - 1))}>
                  <i className="fas fa-minus"></i>
                </button>
                <input type="number" className={styles.quantityInput} value={qty} onChange={e => setQty(Math.max(1, Number(e.target.value) || 1))} min="1"/>
                <button className={styles.quantityButton} onClick={() => setQty(q => q + 1)}>
                  <i className="fas fa-plus"></i>
                </button>
              </div>
            </div>

            <div className={styles.actionButtons}>
              <button className={styles.cartButton} onClick={handleAddToCart}>
                <i className="fas fa-cart-plus"></i>
                Add to Cart
              </button>
            </div>

            <div className={styles.tabsSection}>
              <div className={styles.tabsNav}>
                <button className={`${styles.tabButton} ${tab === "description" ? styles.active : ""}`} onClick={() => setTab("description")}>Description</button>
                <button className={`${styles.tabButton} ${tab === "additional" ? styles.active : ""}`} onClick={() => setTab("additional")}>Additional Info</button>
                <button className={`${styles.tabButton} ${tab === "reviews" ? styles.active : ""}`} onClick={() => setTab("reviews")}>Reviews ({selected?.reviews?.length || 0})</button>
              </div>

              <div className={styles.tabContent}>
                {tab === "description" && <p className={styles.tabText}>{selected?.description}</p>}
                {tab === "additional" && <p className={styles.tabText}>{selected?.additionalInfo}</p>}
                {tab === "reviews" && (
                  <div className={styles.reviewsList}>
                    {selected?.reviews?.length ? selected.reviews.map((rev, idx) => (
                      <div key={idx} className={styles.reviewItem}>
                        <div className={styles.reviewName}>{rev.name}</div>
                        <StarRating rating={rev.rating} />
                        <p className={styles.reviewComment}>{rev.comment}</p>
                      </div>
                    )) : (
                      <div className={styles.noReviews}>
                        <i className="fas fa-comment-slash fa-2x mb-3"></i>
                        <p>No reviews yet. Be the first to review this product!</p>
                      </div>
                    )}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>

        {related.length > 0 && (
          <div className={styles.relatedSection}>
            <h2 className={styles.relatedTitle}>Related Products</h2>
            <div className={styles.relatedGrid}>
              {related.map(p => (
                <div key={p.id} className={styles.relatedCard} onClick={() => navigate(`/product/${p.id}`, { state: { product: p } })}>
                  <img src={p.img} alt={p.name} className={styles.relatedImage}/>
                  <div className={styles.relatedInfo}>
                    <h3 className={styles.relatedName}>{p.name}</h3>
                    <div className={styles.relatedPrice}>${p.price.toFixed(2)}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
