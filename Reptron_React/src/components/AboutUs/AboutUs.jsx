import React from "react";
import { useNavigate } from "react-router-dom";
import styles from './AboutUs.module.css';

function AboutUs() {
  const navigate = useNavigate();

  return (
    <div className={styles.aboutContainer}>
      <div className={styles.glowEffect}></div>

      <div className="container">
        <button className={styles.backButton} onClick={() => navigate("/")}>
          <i className="fas fa-arrow-left"></i>
          Back to Home
        </button>

        <div className={styles.header}>
          <h1 className={styles.title}>About SUPPLEMENT STORE</h1>
          <p className={styles.subtitle}>
            Premium Fitness • Modern Coaching • Athlete Performance
          </p>
        </div>

        <div className={styles.card}>
          <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>
              <i className="fas fa-history"></i>
              Our Story
            </h3>
            <p className={styles.cardText}>
              Supplement Store started as a small vision: creating a place where athletes
              and beginners can get high-quality supplements and elite training
              guidance. Today, we help thousands achieve strength, confidence, and a healthier life.
            </p>
            <p className={styles.cardText}>
              Built by a team of athletes, coaches, and nutrition specialists,
              SUPPLEMENT STORE focuses on science-based solutions, reliability, and real results.
              Our mission is to make fitness accessible, enjoyable, and life-changing.
            </p>
          </div>
        </div>

        <div className={styles.grid}>
          <div className={styles.card}>
            <div className={styles.cardBody}>
              <h3 className={styles.cardTitle}>
                <i className="fas fa-bullseye"></i>
                Our Mission
              </h3>
              <p className={styles.cardText}>
                To empower individuals with world-class supplements, training programs, 
                and expert knowledge that help them reach their maximum physical potential.
              </p>
            </div>
          </div>

          <div className={styles.card}>
            <div className={styles.cardBody}>
              <h3 className={styles.cardTitle}>
                <i className="fas fa-eye"></i>
                Our Vision
              </h3>
              <p className={styles.cardText}>
                To become the most trusted fitness brand worldwide — known for
                innovation, transparency, and transforming lives through health
                and performance.
              </p>
            </div>
          </div>
        </div>

        <div className={`${styles.card} ${styles.fullWidth}`}>
          <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>
              <i className="fas fa-star"></i>
              Why Choose SUPPLEMENT STORE?
            </h3>
            <ul className={styles.featureList}>
              <li className={styles.featureItem}>High-quality, lab-tested supplements</li>
              <li className={styles.featureItem}>Elite training programs built by certified coaches</li>
              <li className={styles.featureItem}>Trusted by professional athletes and trainers</li>
              <li className={styles.featureItem}>Modern fitness technology and personalized guidance</li>
              <li className={styles.featureItem}>Fast shipping, 24/7 support, and guaranteed results</li>
            </ul>
          </div>
        </div>

        <div className={styles.card}>
          <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>
              <i className="fas fa-gem"></i>
              Our Core Values
            </h3>
            <ul className={styles.valuesList}>
              <li className={styles.valueItem}>
                <span className={styles.valueStrong}>Integrity:</span> Always deliver truth and transparency.
              </li>
              <li className={styles.valueItem}>
                <span className={styles.valueStrong}>Quality:</span> We provide products tested and trusted by experts.
              </li>
              <li className={styles.valueItem}>
                <span className={styles.valueStrong}>Passion:</span> Fitness is our lifestyle, not a trend.
              </li>
              <li className={styles.valueItem}>
                <span className={styles.valueStrong}>Innovation:</span> Continually improving training and nutrition systems.
              </li>
              <li className={styles.valueItem}>
                <span className={styles.valueStrong}>Community:</span> We build a strong and supportive fitness family.
              </li>
            </ul>
          </div>
        </div>

        <div className={`${styles.card} ${styles.fullWidth}`}>
          <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>
              <i className="fas fa-users"></i>
              Meet Our Team
            </h3>
            <div className={styles.teamGrid}>
              <div className={styles.teamMember}>
                <img src="https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=300" alt="Ahmed Mohamed" className={styles.memberImage}/>
                <h4 className={styles.memberName}>Ahmed Mohamed</h4>
                <p className={styles.memberRole}>Head Bodybuilding Coach</p>
              </div>

              <div className={styles.teamMember}>
                <img src="https://plus.unsplash.com/premium_photo-1661898576032-fd26e3409175?w=300" alt="Mohamed Ali" className={styles.memberImage}/>
                <h4 className={styles.memberName}>Mohamed Ali</h4>
                <p className={styles.memberRole}>Fitness & Functional Training Expert</p>
              </div>

              <div className={styles.teamMember}>
                <img src="https://images.stockcake.com/public/e/f/c/efcc3abe-b1e3-40b5-bd05-52beae0c0eba_large/confident-fitness-coach-stockcake.jpg" alt="Karim Samy" className={styles.memberImage}/>
                <h4 className={styles.memberName}>Karim Samy</h4>
                <p className={styles.memberRole}>Strength & Conditioning Coach</p>
              </div>
            </div>
          </div>
        </div>

        <div className={styles.card}>
          <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>
              <i className="fas fa-envelope"></i>
              Contact Us
            </h3>
            <p className={styles.cardText}>
              Have questions? Need personalized coaching?  
              Our team is ready to help you start your fitness journey.
            </p>
            
            <div className={styles.contactInfo}>
              <div className={styles.contactItem}>
                <h4 className={styles.contactTitle}>
                  <i className="fas fa-envelope"></i>
                  Email
                </h4>
                <p className={styles.contactText}>support@powerfuel.com</p>
              </div>
              
              <div className={styles.contactItem}>
                <h4 className={styles.contactTitle}>
                  <i className="fas fa-phone"></i>
                  Phone
                </h4>
                <p className={styles.contactText}>+20 123 888 9999</p>
              </div>
              
              <div className={styles.contactItem}>
                <h4 className={styles.contactTitle}>
                  <i className="fas fa-clock"></i>
                  Support Hours
                </h4>
                <p className={styles.contactText}>24/7 Customer Support</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AboutUs;