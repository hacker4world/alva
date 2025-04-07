import { useState, useEffect } from "react";
import axios from "axios";
import { useForm } from "react-hook-form";
import { useNavigate, useLocation } from "react-router-dom";
import WelcomeNavbar from "../layouts/welcomePage/welcomeNavbar";
import Footer from "../layouts/welcomePage/footer";
import "../styles/forgetPwd.css";
import Swal from "sweetalert2";

const ForgotPassword = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    watch,
  } = useForm();
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState("");
  const [passwordRequirements, setPasswordRequirements] = useState([
    { id: 1, text: "At least 8 characters", validated: false },
    { id: 2, text: "At least one uppercase letter", validated: false },
    { id: 3, text: "At least one lowercase letter", validated: false },
    { id: 4, text: "At least one number", validated: false },
    { id: 5, text: "At least one special character (!@#$%&*_-)", validated: false },
  ]);
  const [isPasswordFocused, setIsPasswordFocused] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const apiUrl = import.meta.env.VITE_API_URL;

  const newPassword = watch("newPassword", "");

  // Set the email from location state when component mounts
  useEffect(() => {
    if (location.state?.email) {
      setEmail(location.state.email);
      setValue("email", location.state.email);
    }
  }, [location.state, setValue]);

  // Validate password in real-time
  useEffect(() => {
    if (step === 3 && newPassword) {
      validatePassword(newPassword);
    }
  }, [newPassword, step]);

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

  // Check if all password requirements are met
  const isPasswordValid = () => {
    return passwordRequirements.every((req) => req.validated);
  };

  // Step 1: Request Reset Code
  const requestReset = async (data) => {
    try {
      setEmail(data.email);
      await axios.post(
        `${apiUrl}/api/userManagement/resetPassword/requestCode`,
        {
          email: data.email,
        }
      );
      Swal.fire({
        icon: "success",
        title: "Reset code sent!",
        text: "A reset code has been sent to your email.",
      });
      setStep(2);
    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: error.response?.data || "Something went wrong",
      });
    }
  };

  // Step 2: Verify Code
  const verifyCode = async (data) => {
    try {
      await axios.post(
        `${apiUrl}/api/userManagement/resetPassword/verifyCode`,
        {
          email,
          code: data.code,
        }
      );
      Swal.fire({
        icon: "success",
        title: "Code verified!",
        text: "The verification code is valid.",
      });
      setStep(3);
    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Invalid Code",
        text: "The code entered is invalid. Please try again.",
      });
    }
  };

  // Step 3: Reset Password
  const resetPassword = async (data) => {
    if (data.newPassword !== data.confirmPassword) {
      Swal.fire({
        icon: "error",
        title: "Passwords Do Not Match",
        text: "Please make sure the new password and confirmation password are the same.",
      });
      return;
    }

    if (!isPasswordValid()) {
      Swal.fire({
        icon: "error",
        title: "Password Requirements Not Met",
        text: "Please ensure your password meets all the requirements.",
      });
      return;
    }

    try {
      await axios.put(
        `${apiUrl}/api/userManagement/resetPassword/updatePassword`,
        {
          email,
          newPassword: data.newPassword,
          confirmPassword: data.confirmPassword,
        }
      );
      Swal.fire({
        icon: "success",
        title: "Password Reset Successful!",
        text: "Your password has been successfully reset. You can now log in with your new password.",
      }).then(() => {
        navigate("/login");
      });
    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Error Resetting Password",
        text: "There was an error resetting your password. Please try again later.",
      });
    }
  };

  return (
    <div>
      <WelcomeNavbar
        title="Forgot Password"
        buttonText="Back to Login"
        buttonLink="/login"
      />
      <div className="form-container">
        <div className="form-wrapper">
          <h2 className="heading">Reset Password</h2>

          {/* Step Indicator */}
          <div className="step-indicator">
            {[1, 2, 3].map((s) => (
              <div key={s} className={`step ${step === s ? "active" : ""}`} />
            ))}
          </div>

          {/* Step 1: Request Reset Code */}
          {step === 1 && (
            <form className="form" onSubmit={handleSubmit(requestReset)}>
              <input
                type="email"
                placeholder="Enter your email"
                {...register("email", { required: "Email is required" })}
                className="input"
                defaultValue={email}
              />
              {errors.email && (
                <p className="error-message">{errors.email.message}</p>
              )}
              <button type="submit" className="login-button">
                Send Code
              </button>
            </form>
          )}

          {/* Step 2: Verify Code */}
          {step === 2 && (
            <form className="form" onSubmit={handleSubmit(verifyCode)}>
              <input
                type="text"
                placeholder="Enter verification code"
                {...register("code", { required: "Code is required" })}
                className="input"
              />
              {errors.code && (
                <p className="error-message">{errors.code.message}</p>
              )}
              <button type="submit" className="login-button">
                Verify Code
              </button>
            </form>
          )}

          {/* Step 3: Reset Password */}
          {step === 3 && (
            <form className="form" onSubmit={handleSubmit(resetPassword)}>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="New Password"
                  {...register("newPassword", {
                    required: "New Password is required",
                  })}
                  className="input"
                  onFocus={() => setIsPasswordFocused(true)}
                  onBlur={() => setIsPasswordFocused(false)}
                />
                {errors.newPassword && (
                  <p className="error">{errors.newPassword.message}</p>
                )}

                {/* Password Requirements Checklist */}
                {isPasswordFocused && (
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
              </div>

              <div className="form-group">
                <input
                  type="password"
                  placeholder="Confirm Password"
                  {...register("confirmPassword", {
                    required: "Confirm password is required",
                    validate: (value) =>
                      value === newPassword || "Passwords do not match",
                  })}
                  className="input"
                />
                {errors.confirmPassword && (
                  <p className="error-message">
                    {errors.confirmPassword.message}
                  </p>
                )}
              </div>

              <button 
                type="submit" 
                className="login-button"
                disabled={!isPasswordValid()}
              >
                Reset Password
              </button>
            </form>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ForgotPassword;