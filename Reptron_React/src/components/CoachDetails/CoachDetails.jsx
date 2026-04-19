import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import toast, { Toaster } from "react-hot-toast";
import styles from './CoachDetails.module.css';
import { getCoachById } from '../../services/storeService';

function CoachDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const coaches = [
    {
      id: 1,
      name: "Ahmed Mohamed",
      specialty: "Bodybuilding",
      title: "Professional Bodybuilding Coach",
      bio: "Certified trainer with 8 years of experience in bodybuilding and fitness. Winner of several local and international championships.",
      experience: "8+ years",
      clients: "150+ clients",
      certifications: "5 championships",
      image: "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400&h=400&fit=crop&crop=center",
      phone: "+20 123 456 7890",
      email: "ahmed.mohamed@example.com",
      hourlyRate: "$50/hour",
      availability: ["Monday: 9AM - 5PM", "Tuesday: 9AM - 5PM", "Wednesday: 9AM - 5PM", "Thursday: 9AM - 5PM", "Friday: 9AM - 1PM"],
      fullBio: "Ahmed is a professional bodybuilding coach with over 8 years of experience. He has won multiple championships and specializes in helping athletes achieve their peak physical condition through customized training programs and nutrition plans."
    },
    {
      id: 2,
      name: "Mohamed Ali",
      specialty: "Fitness",
      title: "Fitness and Functional Training Coach",
      bio: "Certified fitness and functional training specialist. Expert in weight loss exercises and general fitness improvement.",
      experience: "6+ years",
      clients: "200+ clients",
      certifications: "3 certifications",
      image: "https://plus.unsplash.com/premium_photo-1661898576032-fd26e3409175?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      phone: "+20 123 456 7891",
      email: "mohamed.ali@example.com",
      hourlyRate: "$45/hour",
      availability: ["Monday: 10AM - 6PM", "Tuesday: 10AM - 6PM", "Wednesday: 10AM - 6PM", "Saturday: 9AM - 2PM"],
      fullBio: "Mohamed specializes in functional training and weight loss programs. With 6 years of experience, he has helped over 200 clients achieve their fitness goals through personalized workout routines and lifestyle coaching."
    },
    {
      id: 3,
      name: "Sameh Khaled",
      specialty: "Sports Nutrition",
      title: "Sports Nutrition Specialist",
      bio: "Certified nutrition specialist with 10 years of experience in sports nutrition. Helps athletes improve performance through balanced nutrition.",
      experience: "10+ years",
      clients: "300+ clients",
      certifications: "7 certifications",
      image: "https://images.unsplash.com/photo-1758875568932-0eefd3e60090?q=80&w=1332&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      phone: "+20 123 456 7892",
      email: "sameh.khaled@example.com",
      hourlyRate: "$60/hour",
      availability: ["Monday: 8AM - 4PM", "Tuesday: 8AM - 4PM", "Wednesday: 8AM - 4PM", "Thursday: 8AM - 4PM"],
      fullBio: "Sameh is a renowned sports nutrition specialist with a decade of experience. He holds 7 certifications and has helped over 300 athletes optimize their performance through scientifically-backed nutrition plans."
    },
    {
      id: 4,
      name: "Samir Mohamed",
      specialty: "Sports Rehabilitation",
      title: "Sports Rehabilitation and Injury Coach",
      bio: "Certified sports rehabilitation and injury treatment trainer. Helps athletes recover and return to sports safely.",
      experience: "7+ years",
      clients: "180+ clients",
      certifications: "4 certifications",
      image: "https://plus.unsplash.com/premium_photo-1664301050654-63085cc3c656?q=80&w=1192&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      phone: "+20 123 456 7893",
      email: "samir.mohamed@example.com",
      hourlyRate: "$55/hour",
      availability: ["Monday: 9AM - 3PM", "Tuesday: 9AM - 3PM", "Thursday: 9AM - 3PM", "Friday: 9AM - 3PM"],
      fullBio: "Samir specializes in sports rehabilitation and injury prevention. With 7 years of experience, he has successfully helped numerous athletes recover from injuries and return to their sports stronger than before."
    },
    {
      id: 5,
      name: "Omar Hassan",
      specialty: "CrossFit",
      title: "CrossFit Level 3 Trainer",
      bio: "CrossFit Level 3 certified trainer with 5 years of experience. Specialized in high-intensity functional training.",
      experience: "5+ years",
      clients: "120+ clients",
      certifications: "6 certifications",
      image: "https://images.unsplash.com/photo-1551763337-e05b91996d32?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      phone: "+20 123 456 7894",
      email: "omar.hassan@example.com",
      hourlyRate: "$40/hour",
      availability: ["Monday: 6AM - 2PM", "Wednesday: 6AM - 2PM", "Friday: 6AM - 2PM", "Saturday: 8AM - 12PM"],
      fullBio: "Omar is a CrossFit Level 3 certified trainer passionate about high-intensity functional training. He has transformed the lives of over 120 clients through his dynamic and challenging workout sessions."
    },
    {
      id: 6,
      name: "Lina Mahmoud",
      specialty: "Yoga & Pilates",
      title: "Yoga and Pilates Instructor",
      bio: "Certified yoga and pilates instructor with 4 years of experience. Focuses on flexibility, balance, and mental wellness.",
      experience: "4+ years",
      clients: "90+ clients",
      certifications: "4 certifications",
      image: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400&h=400&fit=crop&crop=center",
      phone: "+20 123 456 7895",
      email: "lina.mahmoud@example.com",
      hourlyRate: "$35/hour",
      availability: ["Monday: 8AM - 12PM", "Tuesday: 8AM - 12PM", "Thursday: 8AM - 12PM", "Saturday: 9AM - 1PM"],
      fullBio: "Lina brings peace and balance to her clients through yoga and pilates. With 4 years of experience, she focuses on improving flexibility, mental wellness, and overall body awareness."
    },
    {
      id: 7,
      name: "Karim Samy",
      specialty: "Strength Training",
      title: "Strength and Conditioning Coach",
      bio: "Strength and conditioning specialist with 9 years of experience. Works with professional athletes and fitness enthusiasts.",
      experience: "9+ years",
      clients: "220+ clients",
      certifications: "8 certifications",
      image: "https://images.stockcake.com/public/e/f/c/efcc3abe-b1e3-40b5-bd05-52beae0c0eba_large/confident-fitness-coach-stockcake.jpg",
      phone: "+20 123 456 7896",
      email: "karim.samy@example.com",
      hourlyRate: "$65/hour",
      availability: ["Tuesday: 7AM - 3PM", "Wednesday: 7AM - 3PM", "Thursday: 7AM - 3PM", "Friday: 7AM - 3PM"],
      fullBio: "Karim is a strength and conditioning expert with 9 years of experience working with both professional athletes and fitness enthusiasts. His comprehensive approach combines strength training with proper technique and recovery."
    },
    {
      id: 8,
      name: "Nour ElDin",
      specialty: "Cardio Training",
      title: "Cardio and Endurance Coach",
      bio: "Cardio and endurance training expert with 6 years of experience. Specializes in marathon training and cardiovascular health.",
      experience: "6+ years",
      clients: "150+ clients",
      certifications: "5 certifications",
      image: "https://plus.unsplash.com/premium_photo-1661375069014-cade4c4032b4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      phone: "+20 123 456 7897",
      email: "nour.eldin@example.com",
      hourlyRate: "$42/hour",
      availability: ["Monday: 5AM - 1PM", "Tuesday: 5AM - 1PM", "Thursday: 5AM - 1PM", "Saturday: 6AM - 10AM"],
      fullBio: "Nour specializes in cardiovascular training and endurance building. With 6 years of experience, he has successfully trained numerous clients for marathons and helped improve their overall cardiovascular health."
    }
  ];

  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [coach, setCoach] = useState(coaches.find(c => c.id === parseInt(id)));

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
      <div className={styles.coachDetailsContainer}>
        <div className={styles.glowEffect}></div>

        <div className="container">
          <div className={styles.notFound}>
            <i className={`fas fa-user-slash ${styles.notFoundIcon}`}></i>
            <h2 className={styles.notFoundTitle}>Coach Not Found</h2>
            <p className={styles.notFoundText}>
              The coach you're looking for doesn't exist or has been removed.
            </p>
            <button className={styles.notFoundButton} onClick={() => navigate('/coaches')}>
              <i className="fas fa-arrow-left"></i>
              Back to Coaches
            </button>
          </div>
        </div>
      </div>
    );
  }

  const handleBooking = () => {
    if (!selectedDate || !selectedTime) {
      toast.error("Please select both date and time!", { 
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

    toast.success(`Session booked with ${coach.name} on ${selectedDate} at ${selectedTime}`,
      { 
        style: {
          background: "#1e293b",
          color: "#00e5ff",
          border: "1px solid #00e5ff",
          padding: "16px",
          borderRadius: "12px",
          fontWeight: "bold",
        }
      }
    );

    setTimeout(() => {
      navigate("/");
    }, 2000);
  };

  return (
    <div className={styles.coachDetailsContainer}>
      <div className={styles.glowEffect}></div>

      <Toaster
        toastOptions={{
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
        <button className={styles.backButton} onClick={() => navigate('/coaches')}>
          <i className="fas fa-arrow-left"></i>
          Back to Coaches
        </button>

        <div className={styles.coachGrid}>
          <div className={styles.profileCard}>
            <div className={styles.profileContent}>
              <div className={styles.coachHeader}>
                <div className={styles.coachImageContainer}>
                  <img src={coach.image} alt={coach.name} className={styles.coachImage}/>
                </div>
                <div className={styles.coachInfo}>
                  <div className={styles.specialtyBadge}>{coach.specialty}</div>
                  <h1 className={styles.coachName}>{coach.name}</h1>
                  <p className={styles.coachTitle}>{coach.title}</p>
                </div>
              </div>
              <p className={styles.coachBio}>{coach.fullBio}</p>
              <div className={styles.contactInfo}>
                <div className={styles.contactItem}>
                  <i className="fas fa-phone"></i>
                  <span>{coach.phone}</span>
                </div>
                <div className={styles.contactItem}>
                  <i className="fas fa-envelope"></i>
                  <span>{coach.email}</span>
                </div>
                <div className={styles.contactItem}>
                  <i className="fas fa-dollar-sign"></i>
                  <span>{coach.hourlyRate}</span>
                </div>
              </div>
            </div>
          </div>

          <div className={styles.bookingCard}>
            <div className={styles.bookingContent}>
              <h2 className={styles.bookingTitle}>Book a Session</h2>
              <div className={styles.availabilitySection}>
                <h3 className={styles.availabilityTitle}>
                  <i className="fas fa-calendar-check"></i>
                  Availability
                </h3>
                <ul className={styles.availabilityList}>
                  {coach.availability.map((slot, index) => (
                    <li key={index} className={styles.availabilityItem}>
                      {slot}
                    </li>
                  ))}
                </ul>
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="date" className={styles.formLabel}>Select Date</label>
                <input type="date" className={styles.formInput} id="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} min={new Date().toISOString().split('T')[0]}/>
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="time" className={styles.formLabel}>Select Time</label>
                <select className={styles.formSelect} id="time" value={selectedTime} onChange={(e) => setSelectedTime(e.target.value)}>
                  <option value="">Choose time</option>
                  <option value="09:00">9:00 AM</option>
                  <option value="10:00">10:00 AM</option>
                  <option value="11:00">11:00 AM</option>
                  <option value="12:00">12:00 PM</option>
                  <option value="13:00">1:00 PM</option>
                  <option value="14:00">2:00 PM</option>
                  <option value="15:00">3:00 PM</option>
                  <option value="16:00">4:00 PM</option>
                </select>
              </div>

              <button className={styles.submitButton} onClick={handleBooking} disabled={!selectedDate || !selectedTime}>
                <i className="fas fa-calendar-plus"></i>
                Confirm Booking
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CoachDetails;