import React, { useState } from "react";
import axios from "axios";
import "../styles/Form.css";


const CreateUserForm = () => {
  const [userDto, setUserDto] = useState({
    cin: "",
    matriculeNumber: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    adress: "",
    phoneNumber: "",
    accountType: "",
  });

  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [passwordRequirements, setPasswordRequirements] = useState([
      { id: 1, text: "At least 8 characters", validated: false },
      { id: 2, text: "At least one uppercase letter", validated: false },
      { id: 3, text: "At least one lowercase letter", validated: false },
      { id: 4, text: "At least one number", validated: false },
      { id: 5, text: "At least one special character (!@#$%&*_-)", validated: false },
    ]);

     const [isPasswordFocused, setIsPasswordFocused] = useState(false);
  
   const apiUrl = import.meta.env.VITE_API_URL;
   

   const validateForm = () => {
    const errors = {};

    // First Name Validation
    if (!userDto.firstName) {
      errors.firstName = "First Name is required";
    } else if (userDto.firstName.length < 4) {
      errors.firstName = "First Name must be at least 4 characters";
    } else if (!/^[A-Za-z]+$/.test(userDto.firstName)) {
      errors.firstName = "First Name must contain only alphabets";
    }

    // Last Name Validation
    if (!userDto.lastName) {
      errors.lastName = "Last Name is required";
    } else if (userDto.lastName.length < 4) {
      errors.lastName = "Last Name must be at least 4 characters";
    } else if (!/^[A-Za-z]+$/.test(userDto.lastName)) {
      errors.lastName = "Last Name must contain only alphabets";
    }

    // Matricule Number Validation
    if (!userDto.matriculeNumber || !/^\d{4}$/.test(userDto.matriculeNumber)) {
      errors.matriculeNumber = "Matricule number must be 4 digits";
    }

    // Phone Number Validation
    if (!userDto.phoneNumber || !/^\d{8}$/.test(userDto.phoneNumber)) {
      errors.phoneNumber = "Phone number must be 8 digits";
    }

    // Address Validation
    if (!userDto.adress) {
      errors.adress = "Address is required";
    }

    // CIN Validation
    if (!userDto.cin || !/^\d{8}$/.test(userDto.cin)) {
      errors.cin = "CIN must be 8 digits";
    }

    // Email Validation
    if (!userDto.email || !/^\S+@\S+\.\S+$/.test(userDto.email)) {
      errors.email = "Invalid email";
    }

    // Password Validation
    if (!userDto.password) {
      errors.password = "Password is required";
    } else if (!passwordRequirements.every((req) => req.validated)) {
      errors.password = "Password does not meet all requirements";
    }

    setErrors(errors);
    return Object.keys(errors).length === 0;
  };


  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserDto((prevState) => ({
      ...prevState,
      [name]: value,
    }));

  if (name === "password") {
    validatePassword(value);
  }

  // Validation for number fields (cin, matriculeNumber, phoneNumber)
  if (name === "cin" || name === "matriculeNumber" || name === "phoneNumber") {
    if (!/^\d*$/.test(value)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: "Only numbers are allowed",
      }));
    } else {
      setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
    }
  }

  // Validation for name fields (firstName, lastName)
  if (name === "firstName" || name === "lastName") {
    if (!/^[A-Za-z]*$/.test(value)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: "Only alphabets are allowed",
      }));
    } else {
      setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
    }
  }
};

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


  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const response = await axios.post(
       `${apiUrl}/api/authentication/createUser`,
        userDto
      );
      console.log(response.data);
      
        setSuccessMessage("Your account is created! Wait until activation");
        setTimeout(() => setSuccessMessage(""), 3000);
         setUserDto({
        cin: "",
        matriculeNumber: "",
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        adress: "",
        phoneNumber: "",
        accountType: "",
      });
      setErrors({});
    } catch (error) {
      setSuccessMessage("");
      alert(error.response?.data || "An error occurred");
    }
  };

  const isFormValid = () => {
    const hasErrors = Object.values(errors).some((error) => error !== "");
    const isPasswordValid = passwordRequirements.every((req) => req.validated);
    return !hasErrors && isPasswordValid;
  };

  return (
    <div className="form-container">
      <div className="form-wrapper">
        <div className="heading">Create User</div>
        <form className="form" onSubmit={handleSubmit}>
          {successMessage && (
            <div className="success-message">{successMessage}</div>
          )}
           {/* CIN Field */}
           <input
              type="text"
              name="cin"
              value={userDto.cin}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, "").slice(0, 8);
              }}
              className="input"
              placeholder="CIN"
              required
            />
            {errors.cin && <span className="error">{errors.cin}</span>}

            {/* Matricule Number Field */}
            <input
              type="text"
              name="matriculeNumber"
              value={userDto.matriculeNumber}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, "").slice(0, 4);
              }}
              className="input"
              placeholder="Matricule Number"
              required
            />
            {errors.matriculeNumber && (
              <span className="error">{errors.matriculeNumber}</span>
            )}

            {/* First Name Field */}
            <input
              type="text"
              name="firstName"
              value={userDto.firstName}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/[^A-Za-z]/g, "");
              }}
              className="input"
              placeholder="First Name"
              required
            />
            {errors.firstName && (
              <span className="error">{errors.firstName}</span>
            )}

            {/* Last Name Field */}
            <input
              type="text"
              name="lastName"
              value={userDto.lastName}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/[^A-Za-z]/g, "");
              }}
              className="input"
              placeholder="Last Name"
              required
            />
            {errors.lastName && (
              <span className="error">{errors.lastName}</span>
            )}

            {/* Email Field */}
            <input
              type="email"
              name="email"
              value={userDto.email}
              onChange={handleChange}
              className="input"
              placeholder="Email"
              required
            />
            {errors.email && <span className="error">{errors.email}</span>}

            {/* Address Field */}
            <input
              type="text"
              name="adress"
              value={userDto.adress}
              onChange={handleChange}
              className="input"
              placeholder="Address"
              required
            />
            {errors.adress && <span className="error">{errors.adress}</span>}

            {/* Phone Number Field */}
            <input
              type="text"
              name="phoneNumber"
              value={userDto.phoneNumber}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, "").slice(0, 8);
              }}
              className="input"
              placeholder="Phone Number"
              required
            />
            {errors.phoneNumber && (
              <span className="error">{errors.phoneNumber}</span>
            )}

            {/* Account Type Field */}
            <select
              name="accountType"
              value={userDto.accountType}
              onChange={handleChange}
              className="input"
              required
            >
              <option value="">Select Account Type</option>
              <option value="employee">Employee</option>
              <option value="manager">Manager</option>
              <option value="worker">Worker</option>
            </select>
            {errors.accountType && (
              <span className="error">{errors.accountType}</span>
            )}

            {/* Password Field */}
            <input
              type="password"
              name="password"
              value={userDto.password}
              onChange={handleChange}
              onFocus={() => setIsPasswordFocused(true)}
              onBlur={() => setIsPasswordFocused(false)}
              className="input"
              placeholder="Password"
              required
            />
            {errors.password && <span className="error">{errors.password}</span>}

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

            {/* Submit Button */}
            <button
              type="submit"
              className="login-button"
              disabled={!isFormValid()}
            >
              Create User
            </button>
        </form>
      </div>
    </div>
  );}
export default CreateUserForm;