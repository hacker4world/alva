import axios from "axios";
import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import NavbarE from "../layouts/Employee/NavbarE";
import "../styles/Form.css";
import Swal from "sweetalert2";

const UpdateOldPassword = () => {
  const {
    register,
    handleSubmit,
    watch,
    setError,
    formState: { errors },
  } = useForm();
  
  const navigate = useNavigate();
  const location = useLocation(); 
  const apiUrl = import.meta.env.VITE_API_URL;
  
  const [passwordRequirements, setPasswordRequirements] = useState([
    { id: 1, text: "At least 8 characters", validated: false },
    { id: 2, text: "At least one uppercase letter", validated: false },
    { id: 3, text: "At least one lowercase letter", validated: false },
    { id: 4, text: "At least one number", validated: false },
    { id: 5, text: "At least one special character (!@#$%&*_-)", validated: false },
  ]);
  
  const [isPasswordFocused, setIsPasswordFocused] = useState(false);
  const [isOldPasswordVerified, setIsOldPasswordVerified] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);
  const { userType } = location.state || { userType: "admin" }; 
  const oldPassword = watch("oldPassword");

  // Verify old password when it changes
  useEffect(() => {
    const verifyOldPassword = async () => {
      if (!oldPassword || oldPassword.length < 1) {
        setIsOldPasswordVerified(false);
        return;
      }

      setIsVerifying(true);
      try {
        const token = sessionStorage.getItem("token");
        const id = sessionStorage.getItem("userId");

        const response = await axios.post(
          `${apiUrl}/api/userManagement/verifyOldPassword`,
          { id, oldPassword },
          { headers: { Authorization: token } }
        );

        setIsOldPasswordVerified(response.data.isValid);
        if (!response.data.isValid) {
          setError("oldPassword", {
            type: "manual",
            message: "Incorrect old password",
          });
        }
      } catch (error) {
        console.error("Error verifying old password:", error);
        setIsOldPasswordVerified(false);
        setError("oldPassword", {
          type: "manual",
          message: "Error verifying password",
        });
      } finally {
        setIsVerifying(false);
      }
    };

    // Add debounce to avoid too many requests
    const timer = setTimeout(() => {
      verifyOldPassword();
    }, 500);

    return () => clearTimeout(timer);
  }, [oldPassword, apiUrl, setError]);

  const validatePassword = (password) => {
    const updatedRequirements = passwordRequirements.map((req) => {
      switch (req.id) {
        case 1:
          return { ...req, validated: password.length >= 8 };
        case 2:
          return { ...req, validated: /[A-Z]/.test(password) };
        case 3:
          return { ...req, validated: /[a-z]/.test(password) };
        case 4:
          return { ...req, validated: /\d/.test(password) };
        case 5:
          return { ...req, validated: /[!@#$%&*_-]/.test(password) };
        default:
          return req;
      }
    });
    setPasswordRequirements(updatedRequirements);
  };

  const resetPassword = async (data) => {
    if (!passwordRequirements.every((req) => req.validated)) {
      setError("newPassword", {
        type: "manual",
        message: "Password does not meet all requirements",
      });
      return;
    }

    try {
      const token = sessionStorage.getItem("token");
      const id = sessionStorage.getItem("userId");

      await axios.put(
        `${apiUrl}/api/userManagement/resetPassword/updateOldPassword`,
        {
          id,
          oldPassword: data.oldPassword,
          newPassword: data.newPassword,
          confirmPassword: data.confirmPassword,
        },
        { headers: { Authorization: token } }
      );

      Swal.fire({
        icon: "success",
        title: "Password Updated Successfully!",
        text: "Your password has been successfully updated.",
      }).then(() => {
        const role = sessionStorage.getItem("role");
        if (role === "admin") {
          navigate("/admin-dashboard");
        } else if (role === "employee") {
          navigate("/NavbarE");
        } else if (role === "manager") {
          navigate("/NavbarM");
        } else {
          navigate("/NavbarW");
        }
      });
    } catch (error) {
      console.error("Error response:", error.response);
      Swal.fire({
        icon: "error",
        title: "Error Updating Password",
        text: error.response?.data || "An unexpected error occurred.",
      });
    }
  };

  const handleCancel = () => {
    switch (userType) {
      case "employee":
        navigate("/NavbarE");
        break;
      case "worker":
        navigate("/NavbarW");
        break;
      case "manager":
        navigate("/NavbarM");
        break;
      default:
        navigate("/admin-dashboard");
    }
  };

  return (
    <div>
      <NavbarE />
      <div className="form-container">
        <div className="form-box">
          <h2 className="form-title">Reset Password</h2>

          <form className="form" onSubmit={handleSubmit(resetPassword)}>
            <div className="form-group">
              <input
                type="password"
                placeholder="Old Password"
                {...register("oldPassword", {
                  required: "Old Password is required",
                })}
                className="input"
              />
              {isVerifying && <span className="verifying-text">Verifying...</span>}
              {errors.oldPassword && (
                <p className="error">{errors.oldPassword.message}</p>
              )}
              {isOldPasswordVerified && (
                <span className="success-text">✓ Password verified</span>
              )}
            </div>

            <div className="form-group">
              <input
                type="password"
                placeholder="New Password"
                {...register("newPassword", {
                  required: "New Password is required",
                  onChange: (e) => validatePassword(e.target.value),
                })}
                onFocus={() => setIsPasswordFocused(true)}
                onBlur={() => setIsPasswordFocused(false)}
                className="input"
                disabled={!isOldPasswordVerified}
              />
              {errors.newPassword && (
                <p className="error">{errors.newPassword.message}</p>
              )}
            </div>

            {/* Password Requirements Checklist */}
            {isPasswordFocused && isOldPasswordVerified && (
              <div className="password-checklist">
                {passwordRequirements.map((req) => (
                  <div
                    key={req.id}
                    className={`password-requirement ${
                      req.validated ? "validated" : ""
                    }`}
                  >
                    <span className="checkmark">
                      {req.validated ? "✔" : "❌"}
                    </span>
                    {req.text}
                  </div>
                ))}
              </div>
            )}

            <div className="form-group">
              <input
                type="password"
                placeholder="Confirm Password"
                {...register("confirmPassword", {
                  required: "Confirm Password is required",
                  validate: (value) =>
                    value === watch("newPassword") || "Passwords do not match",
                })}
                className="input"
                disabled={!isOldPasswordVerified}
              />
              {errors.confirmPassword && (
                <p className="error">{errors.confirmPassword.message}</p>
              )}
            </div>

            <button
              type="submit"
              className="login-button mt-2"
              disabled={!isOldPasswordVerified || !passwordRequirements.every(req => req.validated)}
            >
              Update Password
            </button>
            <button
              type="button"
              className="login-button"
              onClick={handleCancel}
            >
              Cancel
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default UpdateOldPassword;