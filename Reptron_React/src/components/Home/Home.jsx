import React, { useState, useEffect } from "react";
import { Toaster } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import styles from './Home.module.css'; 
import { getCoaches, getEquipments, getProducts } from "../../services/storeService";
import { FALLBACK_IMAGE } from "../../services/imageMap";
import ChestPressMachine from '../../assets/eqipment/ChestPressMachine.jpg';


const App = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [activeTestimonial, setActiveTestimonial] = useState(0);

  const navigate = useNavigate();

  const [products, setProducts] = useState([]);
  const [testimonials, setTestimonials] = useState([]);
  const [categories, setCategories] = useState([{ id: "all", name: "All Products", icon: "fa-bolt" }]);

  const features = [
    { icon: "fa-star", title: "Premium Quality", desc: "Lab-tested ingredients" },
    { icon: "fa-trophy", title: "Trusted by Athletes", desc: "Used worldwide" },
    { icon: "fa-truck", title: "Fast Shipping", desc: "Free delivery over $50" },
    { icon: "fa-shield", title: "Money Back Guarantee", desc: "30 days return" },
  ];

  const workoutPrograms = [
    {
      img: "../../src/assets/images/Strength Training.jpg",
      title: "Strength Training",
      desc: "Build maximum muscle and increase explosive power.",
    },
    {
      img: "../../src/assets/images/Fat Loss.jpg",
      title: "Fat Loss",
      desc: "Burn calories fast with structured HIIT and cardio workouts.",
    },
    {
      img: "../../src/assets/images/Endurance.jpg",
      title: "Endurance",
      desc: "Is the ability of an organism to exert itself and remain active for a long period of time.",
    },
  ];

  const blogPosts = [
    {
      img: "../../src/assets/images/Nutrition.jpg",
      title: "Top 10 Healthiest Foods for Muscle Growth",
      date: "Feb 2025",
    },
    {
      img: "../../src/assets/images/Supplements.jpg",
      title: "Do You Really Need Creatine?",
      date: "Jan 2025",
    },
    {
      img: "../../src/assets/images/Workout.jpg",
      title: "5 Best HIIT Routines for Fat Burn",
      date: "March 2025",
    },
  ];

  const filteredProducts = products.filter((p) => {
    const matchSearch = p.name.toLowerCase().includes(searchTerm.toLowerCase()) || p.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchCategory = selectedCategory === "all" || p.category === selectedCategory;
    return matchSearch && matchCategory;
  });

  const addToCart = (product) => {
    const userToken = localStorage.getItem("userToken");

    if (!userToken) {
      toast.error("Login required to access store!", {
        style: {
          background: "#1e293b",
          color: "#ff0404ff",
          border: "1px solid #ff0404ff",
          fontWeight: "bold",
        },
      });

      setTimeout(() => {
        navigate("/login");
      }, 2000);
      return;
    }

    if (product.category === "supplements") {
      navigate("/supplements", { state: { product } });
    } else if (product.category === "equipment") {
      navigate("/equipments", { state: { product } });
    }
  };

  useEffect(() => {
    Promise.allSettled([getProducts(), getEquipments(), getCoaches()]).then((results) => {
      const supplements = results[0].status === "fulfilled" ? results[0].value : [];
      const equipments = results[1].status === "fulfilled" ? results[1].value : [];
      const coaches = results[2].status === "fulfilled" ? results[2].value : [];

      const homeProducts = [
        ...supplements.map((p) => ({ ...p, onSale: p.oldPrice > p.price, originalPrice: p.oldPrice, image: p.img })),
        ...equipments.map((p) => ({ ...p, category: "equipment", rating: 4.8, onSale: p.salePrice > p.price, originalPrice: p.salePrice })),
      ];
      setProducts(homeProducts);

      const dynamicCategories = [
        { id: "all", name: "All Products", icon: "fa-bolt" },
        { id: "supplements", name: "Supplements", icon: "fa-capsules" },
        { id: "equipment", name: "Equipment", icon: "fa-dumbbell" },
      ];
      setCategories(dynamicCategories);

      setTestimonials(
        coaches.slice(0, 2).map((coach) => ({
          id: coach.id,
          name: coach.name,
          role: coach.title,
          content: coach.bio,
          image: coach.image || FALLBACK_IMAGE,
        }))
      );
    });
  }, []);

  useEffect(() => {
    if (!testimonials.length) return;
    const timer = setInterval(() => {
      setActiveTestimonial((prev) => (prev + 1) % testimonials.length);
    }, 4000);
    return () => clearInterval(timer);
  }, [testimonials.length]);

  return (
    <div className={`py-5 ${styles.storeContainer}`}>
      <Toaster />
      <div className={styles.glowEffect}></div>
      <div className={`container ${styles.content}`}>
        <div className={styles.heroSection}>
          <h1 className={styles.heroTitle}>
            LEVEL UP YOUR <span className={styles.heroHighlight}>FITNESS</span>
          </h1>
          <p className={styles.heroSubtitle}>
            Premium supplements, elite equipment, and expert workout programs.
          </p>
          
          <div className={styles.searchContainer}>
            <div className={styles.searchInputGroup}>
              <span className={styles.searchIcon}>
                <i className="fa fa-search"></i>
              </span>
              <input value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className={styles.searchInput} placeholder="Search products..."/>
            </div>
          </div>
          
          <div className={styles.categoryButtons}>
            {categories.map((d) => (
              <button key={d.id} onClick={() => setSelectedCategory(d.id)} className={`${styles.categoryButton} ${selectedCategory === d.id ? styles.categoryButtonActive : ''}`}>
                <i className={`fa ${d.icon} ${styles.categoryIcon}`}></i>
                {d.name}
              </button>
            ))}
          </div>
        </div>

        <div className={styles.featuresSection}>
          <div className="row text-center">
            {features.map((f, i) => (
              <div key={i} className="col-md-3 mb-4">
                <div className={styles.featureCard}>
                  <i className={`fa ${f.icon} ${styles.featureIcon}`} />
                  <h5 className={styles.featureTitle}>{f.title}</h5>
                  <p className={styles.featureDesc}>{f.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.productsSection}>
          <h2 className={styles.sectionTitle}> Best Sellers</h2>
          <div className="row">
            {filteredProducts.map((p) => (
              <div key={p.id} className="col-md-3 mb-4">
                <div className={styles.productCard}>
                  <div className={styles.productImageContainer}>
                    <img src={p.image} className={styles.productImage} alt={p.name} /> 
                    
                    {p.onSale && (
                      <div className={styles.saleBadge}>
                        SALE
                      </div>
                    )}
                  </div>
                  <div className={styles.productBody}>
                    <h5 className={styles.productName}>{p.name}</h5>
                    <p className={styles.productDescription}>{p.description}</p>
                    
                    <div className={styles.productRating}>
                      <span className={styles.stars}>
                        {"★".repeat(Math.floor(p.rating))}
                        <span className={styles.halfStar}>☆</span>
                      </span>
                      <span className={styles.ratingNumber}>{p.rating}</span>
                    </div>
                    
                    <div className={styles.productPrice}>
                      <span className={styles.currentPrice}>${p.price}</span>
                      {p.onSale && (
                        <span className={styles.originalPrice}>${p.originalPrice}</span>
                      )}
                    </div>
                    
                    <button className={styles.addToCartButton}  onClick={() => addToCart(p)} >
                      <i className="fa fa-cart-plus me-2"></i>
                      Show Details
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.workoutSection}>
          <h2 className={styles.sectionTitle}><span className="text-white">🏋️</span> Elite Workout Programs</h2>
          <div className="row">
            {workoutPrograms.map((p, i) => (
              <div key={i} className="col-md-4 mb-4">
                <div className={styles.workoutCard}>
                  <div className={styles.workoutImageContainer}>
                    <img src={p.img} className={styles.workoutImage} alt={p.title} />
                    
                  </div>
                  <div className={styles.workoutBody}>
                    <h4 className={styles.workoutTitle}>{p.title}</h4>
                    <p className={styles.workoutDesc}>{p.desc}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.blogSection}>
          <h2 className={styles.sectionTitle}><span className="text-white">📝</span> Latest Articles</h2>
          <div className="row">
            {blogPosts.map((b, i) => (
              <div key={i} className="col-md-4 mb-4">
                <div className={styles.blogCard}>
                  <div className={styles.blogImageContainer}>
                    <img src={b.img} className={styles.blogImage} alt={b.title} />
                    
                  </div>
                  <div className={styles.blogBody}>
                    <h4 className={styles.blogTitle}>{b.title}</h4>
                    <p className={styles.blogDate}>
                      <i className="fa fa-calendar me-2"></i>
                      {b.date}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.testimonialSection}>
          <div className={styles.testimonialCard}>
            <div className={styles.testimonialContent}>
              {testimonials.length > 0 && (
                <>
                  <img src={testimonials[activeTestimonial].image} className={styles.testimonialImage} alt={testimonials[activeTestimonial].name}/>
                  
                  <blockquote className={styles.testimonialQuote}>
                    "{testimonials[activeTestimonial].content}"
                  </blockquote>
                  <h5 className={styles.testimonialName}>
                    {testimonials[activeTestimonial].name}
                  </h5>
                  <p className={styles.testimonialRole}>
                    {testimonials[activeTestimonial].role}
                  </p>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default App;