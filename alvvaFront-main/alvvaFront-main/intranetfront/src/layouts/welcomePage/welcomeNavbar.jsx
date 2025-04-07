import React from "react";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { useNavigate } from "react-router-dom";
import "../../styles/welcomeNavbar.css";

function WelcomeNavbar({ title, buttonText, buttonLink }) {
  const navigate = useNavigate();

  return (
    <div className="welcome-navbar-container">
      <nav className="welcome-top-nav">
        <h2 className="welcome-navbar-title">{title}</h2>
        <button
          className="back-btn welcome-navbar-button" 
          onClick={() => navigate(buttonLink)}
        >
          {buttonText}
        </button>
      </nav>
    </div>
  );
}

export default WelcomeNavbar;