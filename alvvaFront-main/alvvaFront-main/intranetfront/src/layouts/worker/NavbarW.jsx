import React, { useState, useEffect } from "react";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { useNavigate } from "react-router-dom";
import Logout from "../../components/Logout";
import axios from "axios";
import DisplayPersoInfos from "../../components/DisplayPersoInfos";
import "../admin/Navbar.css";

function NavbarW() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [userImage, setUserImage] = useState("");
  const [showProfileCard, setShowProfileCard] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const id = sessionStorage.getItem("userId");

    const fetchData = async () => {
      try {
        setLoading(true);
        const token = sessionStorage.getItem("token");

        const response = await axios.get(
          `${apiUrl}/api/userManagement/${id}/image`,
          {
            headers: { Authorization: token },
          }
        );

        setUserImage(response.data.body);
      } catch (err) {
        setError(err, "Failed to load user image");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const toggleProfileDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  const toggleProfileCard = () => {
    setShowProfileCard(!showProfileCard);
  };

  return (
    <div>
      <nav className="top-nav">
        <h2>Dashboard</h2>
        <div className="nav-icons">
          <i className="fas fa-bell"></i>
          <i className="fas fa-envelope"></i>
          <div className="profile-settings" onClick={toggleProfileDropdown}>
            <i className="fas fa-cog"></i>
            {isDropdownOpen && (
              <ul className="dropdown-menu">
                <li onClick={() => navigate("/updateInfos", { state: { userType: "worker" } })}>Update Profile</li>
                <li onClick={() => navigate("/updateOldPassword", { state: { userType: "worker" } })}>
                  Update Password
                </li>
                <li>
                  <Logout />
                </li>
              </ul>
            )}
          </div>
          <img
            onClick={toggleProfileCard}
            src={
              userImage
                ? `data:image/jpeg;base64,${userImage}`
                : "default-image.jpg"
            }
            alt="Profile"
            className="profile-pic"
          />
        </div>
      </nav>

      {/* Profile Card Modal */}
      {showProfileCard && (
        <div className="profile-card-overlay" onClick={toggleProfileCard}>
          <div className="profile-card" onClick={(e) => e.stopPropagation()}>
            <span className="close-button" onClick={toggleProfileCard}>
              &times;
            </span>
            <DisplayPersoInfos />
          </div>
        </div>
      )}
    </div>
  );
}

export default NavbarW;
