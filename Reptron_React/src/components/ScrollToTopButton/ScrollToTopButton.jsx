import React, { useState, useEffect } from 'react';
import styles from './ScrollToTopButton.module.css'

function ScrollToTopButton() {
    const [isVisible, setIsVisible] = useState(false);

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth' 
        });
    };

    const toggleVisibility = () => {
        if (window.pageYOffset > 200) {
            setIsVisible(true);
        } else {
            setIsVisible(false);
        }
    };

    useEffect(() => {
        window.addEventListener('scroll', toggleVisibility);

        return () => {
            window.removeEventListener('scroll', toggleVisibility);
        };
    }, []);

    return (
        isVisible && (
            <button 
                className={styles['scroll-to-top']} 
                onClick={scrollToTop} 
            >
                <i className="fas fa-arrow-up"></i>
            </button>
        )
    );
}

export default ScrollToTopButton;