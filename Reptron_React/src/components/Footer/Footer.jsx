import React from "react";
import { Link } from "react-router-dom";
import appicon from '../../assets/appicon.jpeg';

export default function Footer() {
  return (
    <footer className="border-top border-secondary" style={{ background: "#020617" }}>

      <style>
        {`
          .footer-link {
            color: #94a3b8;
            text-decoration: none;
            display: block;
            margin-bottom: 6px;
            transition: 0.3s;
          }
          .footer-link:hover {
            color: #00e5ff;
            transform: translateX(4px);
          }
        `}
      </style>

      <div className="container py-5">
        <div className="row gy-4">
          <div className="col-md-3">
            <div className="d-flex align-items-center mb-3">
              <div
                className="d-flex justify-content-center align-items-center"
                style={{width: "40px", height: "40px", background: "linear-gradient(to right, #00e5ff, #0284c7)", borderRadius: "10px"}}>
                <img src={appicon} alt="icon"  width={50}/>
              </div>
              <h4 className="text-white fw-bold ms-3 mb-0">Reptron</h4>
            </div>

            <p className="text-secondary">
              Your ultimate destination for premium fitness supplements and professional gym equipment.
            </p>
          </div>

          <div className="col-md-3">
            <h5 className="text-white fw-semibold mb-3">Quick Links</h5>
            <ul className="list-unstyled">
              <li><Link to="/" className="footer-link">Home</Link></li>
              <li><Link to="/supplements" className="footer-link">Shop</Link></li>
              <li><Link to="/coaches" className="footer-link">Coaches</Link></li>
              <li><Link to="/aboutUs" className="footer-link">Contact</Link></li>
            </ul>
          </div>

          <div className="col-md-3">
            <h5 className="text-white fw-semibold mb-3">Categories</h5>
            <ul className="list-unstyled">
              <li><Link to="/supplements" className="footer-link">Protein Supplements</Link></li>
              <li><Link to="/supplements" className="footer-link">Pre-Workout</Link></li>
              <li><Link to="/equipments" className="footer-link">Gym Equipment</Link></li>
              <li><Link to="/equipments" className="footer-link">Recovery</Link></li>
            </ul>
          </div>
          <div className="col-md-3">
            <h5 className="text-white fw-semibold mb-3">Newsletter</h5>
            <p className="text-secondary">Subscribe for exclusive offers and fitness tips</p>

            <div className="input-group">
              <input type="email" className="form-control bg-dark text-white border-secondary" placeholder="Your email"/>
              <button className="btn" style={{ background: "#00e5ff", color: "#020617", fontWeight: "bold"}}>
                <i className="fas fa-paper-plane"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}

