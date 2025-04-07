import React, { useState } from 'react';
import Login from '../components/LoginForm';
import CreatUserForm from '../components/CreateUserForm';
import '../styles/welcomePage.css';
import WelcomeNavbar from '../layouts/welcomePage/welcomeNavbar';
import Footer from '../layouts/welcomePage/footer';

const WelcomePage = () => {
  const [showSignUp, setShowSignUp] = useState(false);

  const toggleForm = () => {
    setShowSignUp(!showSignUp);
  };

  return (
    <div className="welcome-page">
      <WelcomeNavbar title="Welcome Page" />
      <div className="welcome-container">
        <div className="slogan-section">
          <h1>Welcome to Alva Intranet</h1>
          <p>Your Security is Our Priority!</p>
        </div>
        <div className="form-section">
          {showSignUp ? <CreatUserForm /> : <Login />}
          <p className="toggle-text">
            {showSignUp ? 'Already have an account? ' : "Don't have an account? "}
            <span className="toggle-link" onClick={toggleForm}>
              {showSignUp ? 'Log In' : 'Create Account'}
            </span>
          </p>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default WelcomePage;