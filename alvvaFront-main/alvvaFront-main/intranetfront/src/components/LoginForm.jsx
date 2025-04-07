import React, { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Form.css";

const LoginForm = () => {
  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const apiUrl = import.meta.env.VITE_API_URL;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLoginData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
  
    try {
      const response = await axios.post(`${apiUrl}/api/authentication/login`, loginData);
  
      if (response.status === 200) {
        const { Token, id, role, status, departmentId } = response.data;
  
        sessionStorage.setItem("token", Token);
        sessionStorage.setItem("userId", id);
        sessionStorage.setItem("role", role);
        sessionStorage.setItem("department_id", departmentId);

  
        console.log("User ID:", id);
        console.log("Role:", role);
        console.log("Token:", Token);
        console.log("department_id", departmentId);
  
        if (status === "inactive") {
          navigate("/waitingPage");
        } else {
          switch (role) {
            case "admin":
              navigate("/admin-dashboard");
              break;
            case "employee":
              navigate("/NavbarE");
              break;
            case "manager":
              navigate("/NavbarM");
              break;
            default:
              navigate("/NavbarW");
          }
        }
      }
    } catch (err) {
      if (err.response) {
        if (err.response.status === 401) {
          setError("Invalid email or password.");
        } else if (err.response.status === 403) {
          setError(err.response.data.message); // from backend
        } else {
          setError("An error occurred. Please try again later.");
        }
      } else {
        setError("Server is unreachable. Please try again later.");
      }
    }
  };
  
  const handleForgotPassword = (e) => {
    e.preventDefault();
    if (!loginData.email) {
      setError("Please enter your email first.");
      return;
    }
    navigate("/forgetPassword", { state: { email: loginData.email } });
  };

  return (
    <div className="form-container">
      <div className="form-wrapper">
        <div className="heading">Sign In</div>
        <form className="form" onSubmit={handleSubmit}>
          {error && <div className="error-message">{error}</div>}
          <input
            type="text"
            name="email"
            value={loginData.email}
            onChange={handleChange}
            className="input"
            placeholder="Email"
            required
          />
          <input
            type="password"
            name="password"
            value={loginData.password}
            onChange={handleChange}
            className="input"
            placeholder="Password"
            required
          />
          <span className="forgot-password">
          <Link 
              to={loginData.email ? {
                pathname: "/forgetPassword",
                state: { email: loginData.email }
              } : "#"}
              onClick={handleForgotPassword}
            >
              Forgot Password?
              </Link>
          </span>
          <button type="submit" className="login-button">
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
