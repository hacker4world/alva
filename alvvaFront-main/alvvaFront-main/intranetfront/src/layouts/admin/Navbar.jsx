import React, { useState, useEffect } from "react";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { Link, useNavigate } from "react-router-dom";
import Logout from "../../components/Logout";
import axios from "axios";
import DisplayPersoInfos from "../../components/DisplayPersoInfos";
import "./Navbar.css";

function Navbar() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isUserManagementOpen, setIsUserManagementOpen] = useState(false);
  const [isPostManagementOpen, setIsPostManagementOpen] = useState(false);
  const [userImage, setUserImage] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showProfileCard, setShowProfileCard] = useState(false);
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
        setError("Failed to load user image");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const toggleUserManagement = () => {
    setIsUserManagementOpen(!isUserManagementOpen);
  };
  
  const togglePostManagement = () => {
    setIsPostManagementOpen(!isPostManagementOpen);
  };

  const toggleDropdown = () => {
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
          <div className="profile-settings" onClick={toggleDropdown}>
            <i className="fas fa-cog"></i>
            {isDropdownOpen && (
              <ul className="dropdown-menu">
                <li onClick={() => navigate("/updateInfos")}>Update Profile</li>
                <li>
                  <Logout />
                </li>
              </ul>
            )}
          </div>
          <img
            onClick={toggleProfileCard}
            src={userImage ? `data:image/jpeg;base64,${userImage}` : "default-image.jpg"}
            alt="Profile"
            className="profile-pic"
          />
        </div>
      </nav>

      <div className={`sidebar ${isSidebarOpen ? "open" : "closed"}`}>
        <div className="logo">
          <h2>
            Admin <span>Dashboard</span>
          </h2>
        </div>
        <ul className="menu">
            <li onClick={toggleUserManagement} style={{ cursor: "pointer" }}>
              <i className="fas fa-layer-group"></i> <span>User Management</span>
            </li>
            {isUserManagementOpen && (
              <ul className="submenu">
                <li>
                  <Link to="/createUserAccount">Create User Account</Link>
                </li>
                <li>
                  <Link to="/AccountsList">Accounts List</Link>
                </li>
              </ul>
            )}
          </ul>

        <ul className="menu">
          <li onClick={togglePostManagement} style={{ cursor: "pointer" }}>
            <i className="fas fa-layer-group"></i> <span>Post Management</span>
          </li>
          {isPostManagementOpen && (
            <ul className="submenu">
              <li>
                <Link to="/createPost">Create Post</Link>
              </li>
              <li>
                <Link to="/updatePost:id">Update Post</Link>
              </li>
            </ul>
          )}
        </ul>
      </div>

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

export default Navbar;
