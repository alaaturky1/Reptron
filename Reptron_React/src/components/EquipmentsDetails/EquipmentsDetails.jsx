import React, { useState, useContext, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { cartContext } from "../../context/cartContext";
import toast, { Toaster } from "react-hot-toast";
import { getEquipmentById, getEquipments } from "../../services/storeService";
import { ITEM_TYPE_EQUIPMENT } from "../../api/cartApi.js";
import styles from './EquipmentsDetails.module.css';

export default function EquipmentPage() {
  const { addToCart } = useContext(cartContext);
  const { id } = useParams();
  const navigate = useNavigate();

  const [equipments, setEquipments] = useState([]);
  const [selected, setSelected] = useState(null);
  const [main, setMain] = useState("");
  useEffect(() => {
    getEquipments().then(setEquipments);
    getEquipmentById(id).then((item) => {
      setSelected(item);
      setMain(item?.img || "");
    });
  }, [id]);

  const [qty, setQty] = useState(1);
  const [tab, setTab] = useState("description");
  
  const related = equipments.filter(eq => selected && eq.id !== selected.id);

  const handleAddToCart = async () => {
  if (!selected) return;
  try {
    await addToCart(selected, qty, ITEM_TYPE_EQUIPMENT);
  } catch {
    return;
  }

  toast.success(`${selected?.name} added to cart successfully`, {
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
  setQty(1);
};

  const handleRelatedClick = (eq) => {
    setSelected(eq);
    setMain(eq.img);
    setQty(1);
    setTab("description");
    navigate(`/equipments/${eq.id}`);
  };

  const StarRating = ({ rating }) => {
    return (
      <div className={styles.starRating}>
        {[...Array(5)].map((_, index) => (
          <i 
            key={index} 
            className={`fas fa-star ${index < rating ? styles.star : 'text-secondary'}`}
          ></i>
        ))}
      </div>
    );
  };

  return (
    <div className={styles.equipmentContainer}>
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
        <button className={styles.backButton} onClick={() => {navigate("/equipments")}}>
          <i className="fas fa-arrow-left"></i>
          Back to Equipments
        </button>

        <div className={styles.equipmentGrid}>
          <div className={styles.imageSection}>
            <img src={main} alt={selected?.name} className={styles.mainImage}/>
          </div>

          <div className={styles.infoSection}>
            <h1 className={styles.equipmentTitle}>{selected?.name}</h1>
            <div className={styles.specialtyBadge}>{selected?.specialty}</div>
            <p className={styles.equipmentBio}>{selected?.bio}</p>

            <div className={styles.priceSection}>
              <span className={styles.currentPrice}>${(selected?.price || 0).toFixed(2)}</span>
              {selected?.salePrice && (
                <>
                  <span className={styles.salePrice}>${selected.salePrice.toFixed(2)}</span>
                </>
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
                <button className={`${styles.tabButton} ${tab === "description" ? styles.active : ""}`} onClick={() => setTab("description")}>
                  Description
                </button>
                <button className={`${styles.tabButton} ${tab === "additional" ? styles.active : ""}`} onClick={() => setTab("additional")}>
                  Additional Info
                </button>
                <button className={`${styles.tabButton} ${tab === "reviews" ? styles.active : ""}`} onClick={() => setTab("reviews")}>
                  Reviews ({selected?.reviews?.length || 0})
                </button>
              </div>

              <div className={styles.tabContent}>
                {tab === "description" && (
                  <p className={styles.tabText}>{selected?.description}</p>
                )}
                {tab === "additional" && (
                  <p className={styles.tabText}>{selected?.additionalInfo}</p>
                )}
                {tab === "reviews" && (
                  <div className={styles.reviewsList}>
                    {selected?.reviews?.length ? (
                      selected.reviews.map((r, i) => (
                        <div key={i} className={styles.reviewItem}>
                          <div className={styles.reviewName}>{r.name}</div>
                          <StarRating rating={r.rating} />
                          <p className={styles.reviewComment}>{r.comment}</p>
                        </div>
                      ))
                    ) : (
                      <div className={styles.noReviews}>
                        <i className="fas fa-comment-slash fa-2x mb-3"></i>
                        <p>No reviews yet. Be the first to review this equipment!</p>
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
            <h2 className={styles.relatedTitle}>Related Equipments</h2>
            <div className={styles.relatedGrid}>
              {related.slice(0, 4).map(eq => (
                <div key={eq.id} className={styles.relatedCard} onClick={() => handleRelatedClick(eq)}>
                  <img src={eq.img} alt={eq.name} className={styles.relatedImage}/>
                  <div className={styles.relatedInfo}>
                    <h3 className={styles.relatedName}>{eq.name}</h3>
                    <div className={styles.relatedPrice}>${eq.price.toFixed(2)}</div>
                    <div className={styles.relatedSpecialty}>{eq.specialty}</div>
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