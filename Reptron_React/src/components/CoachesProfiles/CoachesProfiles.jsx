import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import styles from './CoachesProfiles.module.css';
import { getCoachById } from '../../services/storeService';

function CoachesProfiles() {
    const { id } = useParams();
    const navigate = useNavigate();

    const coaches = [
        {
            id: 1,
            name: "Ahmed Mohamed",
            specialty: "Bodybuilding",
            title: "Professional Bodybuilding Coach",
            bio: "Certified trainer with 8 years of experience in bodybuilding and fitness. Winner of several local and international championships.",
            experience: "8+ years experience",
            clients: "150+ satisfied clients",
            certifications: "5 championships",
            image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400&h=400&fit=crop&crop=center",
            phone: "+20 123 456 7890",
            email: "ahmed.mohamed@example.com",
            fullBio: "Ahmed is a professional bodybuilding coach with over 8 years of experience. He has won multiple championships and specializes in helping athletes achieve their peak physical condition through customized training programs and nutrition plans."
        },
        {
            id: 2,
            name: "Mohamed Ali",
            specialty: "Fitness",
            title: "Fitness and Functional Training Coach",
            bio: "Certified fitness and functional training specialist. Expert in weight loss exercises and general fitness improvement.",
            experience: "6+ years experience",
            clients: "200+ satisfied clients",
            certifications: "3 certifications",
            image: "https://plus.unsplash.com/premium_photo-1661898576032-fd26e3409175?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            phone: "+20 123 456 7891",
            email: "mohamed.ali@example.com",
            fullBio: "Mohamed specializes in functional training and weight loss programs. With 6 years of experience, he has helped over 200 clients achieve their fitness goals through personalized workout routines and lifestyle coaching."
        },
        {
            id: 3,
            name: "Sameh Khaled",
            specialty: "Sports Nutrition",
            title: "Sports Nutrition Specialist",
            bio: "Certified nutrition specialist with 10 years of experience in sports nutrition. Helps athletes improve performance through balanced nutrition.",
            experience: "10+ years experience",
            clients: "300+ satisfied clients",
            certifications: "7 certifications",
            image: "https://images.unsplash.com/photo-1758875568932-0eefd3e60090?q=80&w=1332&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            phone: "+20 123 456 7892",
            email: "sameh.khaled@example.com",
            fullBio: "Sameh is a renowned sports nutrition specialist with a decade of experience. He holds 7 certifications and has helped over 300 athletes optimize their performance through scientifically-backed nutrition plans."
        },
        {
            id: 4,
            name: "Samir Mohamed",
            specialty: "Sports Rehabilitation",
            title: "Sports Rehabilitation and Injury Coach",
            bio: "Certified sports rehabilitation and injury treatment trainer. Helps athletes recover and return to sports safely.",
            experience: "7+ years experience",
            clients: "180+ satisfied clients",
            certifications: "4 certifications",
            image: "https://plus.unsplash.com/premium_photo-1664301050654-63085cc3c656?q=80&w=1192&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            phone: "+20 123 456 7893",
            email: "samir.mohamed@example.com",
            fullBio: "Samir specializes in sports rehabilitation and injury prevention. With 7 years of experience, he has successfully helped numerous athletes recover from injuries and return to their sports stronger than before."
        },
        {
            id: 5,
            name: "Omar Hassan",
            specialty: "CrossFit",
            title: "CrossFit Level 3 Trainer",
            bio: "CrossFit Level 3 certified trainer with 5 years of experience. Specialized in high-intensity functional training.",
            experience: "5+ years experience",
            clients: "120+ satisfied clients",
            certifications: "6 certifications",
            image: "https://images.unsplash.com/photo-1551763337-e05b91996d32?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            phone: "+20 123 456 7894",
            email: "omar.hassan@example.com",
            fullBio: "Omar is a CrossFit Level 3 certified trainer passionate about high-intensity functional training. He has transformed the lives of over 120 clients through his dynamic and challenging workout sessions."
        },
        {
            id: 6,
            name: "Lina Mahmoud",
            specialty: "Yoga & Pilates",
            title: "Yoga and Pilates Instructor",
            bio: "Certified yoga and pilates instructor with 4 years of experience. Focuses on flexibility, balance, and mental wellness.",
            experience: "4+ years experience",
            clients: "90+ satisfied clients",
            certifications: "4 certifications",
            image: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400&h=400&fit=crop&crop=center",
            phone: "+20 123 456 7895",
            email: "lina.mahmoud@example.com",
            fullBio: "Lina brings peace and balance to her clients through yoga and pilates. With 4 years of experience, she focuses on improving flexibility, mental wellness, and overall body awareness."
        },
        {
            id: 7,
            name: "Karim Samy",
            specialty: "Strength Training",
            title: "Strength and Conditioning Coach",
            bio: "Strength and conditioning specialist with 9 years of experience. Works with professional athletes and fitness enthusiasts.",
            experience: "9+ years experience",
            clients: "220+ satisfied clients",
            certifications: "8 certifications",
            image: "https://images.stockcake.com/public/e/f/c/efcc3abe-b1e3-40b5-bd05-52beae0c0eba_large/confident-fitness-coach-stockcake.jpg",
            phone: "+20 123 456 7896",
            email: "karim.samy@example.com",
            fullBio: "Karim is a strength and conditioning expert with 9 years of experience working with both professional athletes and fitness enthusiasts. His comprehensive approach combines strength training with proper technique and recovery."
        },
        {
            id: 8,
            name: "Nour ElDin",
            specialty: "Cardio Training",
            title: "Cardio and Endurance Coach",
            bio: "Cardio and endurance training expert with 6 years of experience. Specializes in marathon training and cardiovascular health.",
            experience: "6+ years experience",
            clients: "150+ satisfied clients",
            certifications: "5 certifications",
            image: "https://plus.unsplash.com/premium_photo-1661375069014-cade4c4032b4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            phone: "+20 123 456 7897",
            email: "nour.eldin@example.com",
            fullBio: "Nour specializes in cardiovascular training and endurance building. With 6 years of experience, he has successfully trained numerous clients for marathons and helped improve their overall cardiovascular health."
        }
    ];

    const [coach, setCoach] = useState(coaches.find((c) => c.id === parseInt(id)));

    useEffect(() => {
        const numericId = parseInt(id, 10);
        getCoachById(id).then((apiCoach) => {
            setCoach(apiCoach || coaches.find((c) => c.id === numericId));
        });
        // coaches is file-local fallback data; effect should only re-run when route id changes
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    if (!coach) {
        return (
            <div className={styles.coachesContainer}>
                <div className={styles.glowEffect}></div>
                <div className={`container ${styles.content}`}>
                    <div className={styles.notFound}>
                        <h2 className={styles.notFoundTitle}>Coach Not Found</h2>
                        <p className={styles.notFoundText}>
                            The coach you're looking for doesn't exist or has been removed.
                        </p>
                        <button className={styles.notFoundButton} onClick={() => navigate('/coaches')}>
                            ← Back to Coaches
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    const extractNumber = (text) => {
        const match = text.match(/\d+/);
        return match ? match[0] : "0";
    };

    return (
        <div className={styles.coachesContainer}>
            <div className={styles.glowEffect}></div>
            
            <div className={`container ${styles.content}`}>
                <button className={styles.backButton} onClick={() => navigate('/coaches')}>
                    ← Back to Coaches
                </button>

                <div className={styles.grid}>
                    <div className={styles.coachCard}>
                        <div className={styles.coachContent}>
                            <div className={styles.profileSection}>
                                <div className={styles.coachImageContainer}>
                                    <img src={coach.image} alt={coach.name} className={styles.coachImage} loading="lazy"/>
                                </div>
                                <div className={styles.coachInfo}>
                                    <div className={styles.specialtyBadge}>
                                        {coach.specialty}
                                    </div>
                                    <h1 className={styles.coachName}>{coach.name}</h1>
                                    <p className={styles.coachTitle}>{coach.title}</p>
                                </div>
                            </div>
                            
                            <div className={styles.bioSection}>
                                <h4 className={styles.bioTitle}>
                                    <i className="bi bi-person-circle"></i>
                                    About {coach.name.split(" ")[0]}
                                </h4>
                                <p className={styles.bioText}>{coach.fullBio}</p>
                            </div>
                        </div>
                    </div>
                    <div className={styles.coachCard}>
                        <div className={styles.coachContent}>
                            <div className={styles.statsSection}>
                                <h4 className={styles.statsTitle}>
                                    <i className="bi bi-award"></i>
                                    Professional Details
                                </h4>
                                <div className={styles.statsGrid}>
                                    <div className={styles.statItem}>
                                        <div className={styles.statValue}>
                                            {extractNumber(coach.experience)}+
                                        </div>
                                        <div className={styles.statLabel}>
                                            Years Experience
                                        </div>
                                    </div>
                                    <div className={styles.statItem}>
                                        <div className={styles.statValue}>
                                            {extractNumber(coach.clients)}+
                                        </div>
                                        <div className={styles.statLabel}>
                                            Happy Clients
                                        </div>
                                    </div>
                                    <div className={styles.statItem}>
                                        <div className={styles.statValue}>
                                            {extractNumber(coach.certifications)}
                                        </div>
                                        <div className={styles.statLabel}>
                                            Certifications
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className={styles.contactSection}>
                                <h4 className={styles.contactTitle}>
                                    <i className="bi bi-telephone"></i>
                                    Contact Information
                                </h4>
                                <div className={styles.contactGrid}>
                                    <div className={styles.contactItem}>
                                        <h5 className={styles.contactSubtitle}>
                                            <i className="bi bi-phone"></i>
                                            Phone
                                        </h5>
                                        <p className={styles.contactText}>{coach.phone}</p>
                                    </div>
                                    <div className={styles.contactItem}>
                                        <h5 className={styles.contactSubtitle}>
                                            <i className="bi bi-envelope"></i>
                                            Email
                                        </h5>
                                        <p className={styles.contactText}>{coach.email}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CoachesProfiles;