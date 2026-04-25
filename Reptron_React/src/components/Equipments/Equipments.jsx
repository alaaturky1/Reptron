import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './Equipments.module.css';
import { useEffect } from "react";
import { getEquipments } from "../../services/storeService";

export default function Equipments() {
  const [equipments, setEquipments] = useState([]);
  useEffect(() => {
    getEquipments().then(setEquipments);
  }, []);

  const [activeFilter, setActiveFilter] = useState("all");
  const navigate = useNavigate();

  const handleViewDetails = (id) => {
    navigate(`/equipments/${id}`);
  };

  const filteredEquipments = activeFilter === "all" 
    ? equipments 
    : equipments.filter((eq) => eq.specialty.toLowerCase() === activeFilter.toLowerCase());

  return (
    <div className={styles.equipmentsContainer}>
      <div className={styles.glowEffect}></div>
      <div className="container">
        <div className={styles.header}>
          <h1 className={styles.title}>Our Professional Equipments</h1>
          <p className={styles.subtitle}>
            Check out the latest advanced equipment for all types of exercises
          </p>
        </div>

        <div className={styles.filterContainer}>
          {["all", "Chest", "Back", "Shoulder", "Leg"].map((filter) => (
            <button key={filter} className={`${styles.filterButton} ${activeFilter === filter ? styles.active : ""}`} onClick={() => setActiveFilter(filter)}>
              {filter === "all" ? "All Equipments" : filter}
            </button>
          ))}
        </div>

        <div className={styles.equipmentsGrid}>
          {filteredEquipments.length === 0 ? (
            <div className={styles.emptyState}>
              <i className={`fas fa-dumbbell ${styles.emptyIcon}`}></i>
              <h2 className={styles.emptyTitle}>No Equipments Found</h2>
              <p className={styles.emptyText}>
                No equipment found for the selected category. Try another filter to see available equipment.
              </p>
              <button className={styles.resetButton} onClick={() => setActiveFilter("all")}>
                <i className="fas fa-redo"></i>
                Show All Equipments
              </button>
            </div>
          ) : (
            filteredEquipments.map((eq) => (
              <div key={eq.id} className={styles.equipmentCard}>
                <div className={styles.imageContainer}>
                  <img src={eq.image} alt={eq.name} className={styles.equipmentImage} />
                  
                  <div className={styles.specialtyBadge}>
                    {eq.specialty}
                  </div>
                </div>

                <div className={styles.cardContent}>
                  <h3 className={styles.equipmentName}>{eq.name}</h3>
                  <p className={styles.equipmentBio}>{eq.bio}</p>
                  <button className={styles.viewButton} onClick={() => handleViewDetails(eq.id)}>
                    <i className="fas fa-eye"></i>
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