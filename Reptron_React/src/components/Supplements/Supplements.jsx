import React from "react";
import { useNavigate } from "react-router-dom";
import { useProducts } from "../../hooks/useProducts.js";
import styles from './Supplements.module.css';

export default function Supplements() {
    const navigate = useNavigate();
    const { data: products, loading, error } = useProducts();

    return (
        <div className={styles.supplementsContainer}>
            <div className={styles.glowEffect}></div>

            <div className="container">
                <div className={styles.header}>
                    <h1 className={styles.title}>Welcome to our Supplements Store</h1>
                    <p className={styles.subtitle}>Premium supplements for peak performance</p>
                </div>

                <div className={styles.productsGrid}>
                    {loading ? (
                        <div className={styles.emptyState}>
                            <i className={`fas fa-spinner fa-spin ${styles.emptyIcon}`}></i>
                            <h2 className={styles.emptyTitle}>Loading products…</h2>
                        </div>
                    ) : error ? (
                        <div className={styles.emptyState}>
                            <i className={`fas fa-exclamation-triangle ${styles.emptyIcon}`}></i>
                            <h2 className={styles.emptyTitle}>Could not load products</h2>
                            <p className={styles.emptyText}>{error}</p>
                        </div>
                    ) : products.length === 0 ? (
                        <div className={styles.emptyState}>
                            <i className={`fas fa-box-open ${styles.emptyIcon}`}></i>
                            <h2 className={styles.emptyTitle}>No Products Available</h2>
                            <p className={styles.emptyText}>
                                We're currently updating our inventory. Please check back soon!
                            </p>
                        </div>
                    ) : (
                        products.map((product) => (
                            <div key={product.id} className={styles.productCard}>

                                <div className={styles.productImageContainer}>
                                    <img src={product.img} alt={product.name} className={styles.productImage}/>
                                </div>

                                <div className={styles.productInfo}>
                                    <h3 className={styles.productName}>{product.name}</h3>
                                    
                                    <div className={styles.priceContainer}>
                                        <span className={styles.currentPrice}>
                                            ${product.price.toFixed(2)}
                                        </span>
                                        <span className={styles.oldPrice}>
                                            ${product.oldPrice.toFixed(2)}
                                        </span>
                                    </div>

                                    <button className={styles.detailsButton} onClick={() => navigate(`/product/${product.id}`, { 
                                            state: { product: product } })}>
                                        <i className="fas fa-cart-plus"></i>
                                        View Details
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}
