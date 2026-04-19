import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Coaches.module.css';
import { getCoaches } from '../../services/storeService';

function Coaches() {
  const [coaches, setCoaches] = useState([]);
  useEffect(() => {
    getCoaches().then(setCoaches);
  }, []);

  const [activeFilter, setActiveFilter] = useState("all");
  const navigate = useNavigate();

  const handleBookSession = (coachId) => {
    navigate(`/coach/${coachId}#booking`);
  };

  const handleViewProfile = (coachId) => {
    navigate(`/coachesProfiles/${coachId}`);
  };

  const filteredCoaches = activeFilter === "all" 
    ? coaches
    : coaches.filter((coach) => coach.specialty.toLowerCase().includes(activeFilter.toLowerCase()));

  const specialties = ["all", ...Array.from(new Set(coaches.map((coach) => coach.specialty)))];

  return (
    <div className={styles.coachesContainer}>
      <div className={styles.glowEffect}></div>

      <div className="container">
        <div className={styles.header}>
          <h1 className={styles.title}>Our Professional Coaches</h1>
          <p className={styles.subtitle}>
            Meet our certified coaches who will help you achieve your fitness goals
          </p>
        </div>

        <div className={styles.filterContainer}>
          {specialties.map((specialty) => (
            <button key={specialty} className={`${styles.filterButton} ${activeFilter === specialty ? styles.active : ""}`} onClick={() => setActiveFilter(specialty)}>
              {specialty === "all" ? "All Coaches" : specialty}
            </button>
          ))}
        </div>

        <div className={styles.coachesGrid}>
          {filteredCoaches.length === 0 ? (
            <div className={styles.emptyState}>
              <i className={`fas fa-user-friends ${styles.emptyIcon}`}></i>
              <h2 className={styles.emptyTitle}>No Coaches Found</h2>
              <p className={styles.emptyText}>
                No coaches found for the selected category. Try another filter to see available coaches.
              </p>
              <button className={styles.resetButton} onClick={() => setActiveFilter("all")}>
                <i className="fas fa-redo"></i>
                Show All Coaches
              </button>
            </div>
          ) : (
            filteredCoaches.map((coach) => (
              <div key={coach.id} className={styles.coachCard}>
                <div className={styles.imageContainer}>
                  <img src={coach.image} alt={coach.name} className={styles.coachImage}/>
                  <div className={styles.specialtyBadge}>
                    {coach.specialty}
                  </div>
                </div>

                <div className={styles.cardContent}>
                  <h3 className={styles.coachName}>{coach.name}</h3>
                  <p className={styles.coachTitle}>{coach.title}</p>
                  <div className={styles.actionButtons}>
                    <button className={styles.profileButton} onClick={() => handleViewProfile(coach.id)}>
                      <i className="fas fa-user-circle"></i>
                      View Profile
                    </button>
                    <button className={styles.bookButton} onClick={() => handleBookSession(coach.id)}>
                      <i className="fas fa-calendar-alt"></i>
                      Book Session
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default Coaches;