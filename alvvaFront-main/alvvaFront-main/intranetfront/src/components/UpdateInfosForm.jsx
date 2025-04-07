import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../styles/UpateForm.css";
import Swal from "sweetalert2";

const UpdateInfosForm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    adress: "",
    cin: "",
    email: "",
    image: null,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [formErrors, setFormErrors] = useState({});
  const location = useLocation();
  const apiUrl = import.meta.env.VITE_API_URL;

  const { userType } = location.state || { userType: "admin" };

  useEffect(() => {
    const id = sessionStorage.getItem("userId");
    if (!id) {
      setError("User ID not found. Please log in.");
      setLoading(false);
      return;
    }

    const fetchUser = async () => {
      try {
        const token = sessionStorage.getItem("token");
        if (!token) throw new Error("Unauthorized access");

        const response = await fetch(`${apiUrl}/api/userManagement/getUserbyId/${id}`, {
          headers: { Authorization: token },
        });

        if (!response.ok) throw new Error("User not found");
        const data = await response.json();
        setFormData(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [apiUrl]);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    
    // For number fields (cin, phoneNumber)
    if (name === "cin" || name === "phoneNumber") {
      if (!/^\d*$/.test(value)) {
        setFormErrors((prevErrors) => ({
          ...prevErrors,
          [name]: "Only numbers are allowed",
        }));
      } else {
        setFormErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
      }
    }

    // For name fields (firstName, lastName)
    if (name === "firstName" || name === "lastName") {
      if (!/^[A-Za-z]*$/.test(value)) {
        setFormErrors((prevErrors) => ({
          ...prevErrors,
          [name]: "Only alphabets are allowed",
        }));
      } else {
        setFormErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
      }
    }

    setFormData((prevData) => ({
      ...prevData,
      [name]: name === "image" ? files[0] : value,
    }));
  };

  const validateForm = () => {
    const errors = {};

    // First Name Validation
    if (!formData.firstName) {
      errors.firstName = "First Name is required";
    } else if (formData.firstName.length < 4) {
      errors.firstName = "First Name must be at least 4 characters";
    } else if (!/^[A-Za-z]+$/.test(formData.firstName)) {
      errors.firstName = "First Name must contain only alphabets";
    }

    // Last Name Validation
    if (!formData.lastName) {
      errors.lastName = "Last Name is required";
    } else if (formData.lastName.length < 4) {
      errors.lastName = "Last Name must be at least 4 characters";
    } else if (!/^[A-Za-z]+$/.test(formData.lastName)) {
      errors.lastName = "Last Name must contain only alphabets";
    }

    // Phone Number Validation
    if (!formData.phoneNumber || !/^\d{8}$/.test(formData.phoneNumber)) {
      errors.phoneNumber = "Phone number must be 8 digits";
    }

    // Address Validation
    if (!formData.adress) {
      errors.adress = "Address is required";
    }

    // CIN Validation
    if (!formData.cin || !/^\d{8}$/.test(formData.cin)) {
      errors.cin = "CIN must be 8 digits";
    }

    // Email Validation
    if (!formData.email || !/^\S+@\S+\.\S+$/.test(formData.email)) {
      errors.email = "Invalid email";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    const id = sessionStorage.getItem("userId");
    if (!id) {
      setError("User ID not found. Please log in.");
      return;
    }

    try {
      const token = sessionStorage.getItem("token");
      const response = await fetch(`${apiUrl}/api/userManagement/updateUser/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: token,
        },
        body: JSON.stringify({ ...formData, image: undefined }),
      });

      if (!response.ok) throw new Error("Update failed");

      if (formData.image) {
        const imageFormData = new FormData();
        imageFormData.append("image", formData.image);

        await fetch(`${apiUrl}/api/userManagement/${id}/updateImage`, {
          method: "PUT",
          headers: { Authorization: token },
          body: imageFormData,
        });
      }
      
      const role = sessionStorage.getItem("role");
      
      Swal.fire({
        icon: "success",
        title: "User Updating Successful!",
        text: " ",
      })
      
      if (role === "admin") {
        navigate("/admin-dashboard");
      } else if (role === "employee") {
        navigate("/NavbarE");
      } else if (role === "manager") {
        navigate("/NavbarM");
      } else {
        navigate("/NavbarW");
      }
    } catch (error) {
      setError(error.message);
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

  // Check if form is valid
  const isFormValid = () => {
    const hasErrors = Object.values(formErrors).some((error) => error !== "");
    return !hasErrors;
  };

  if (loading) return <div className="spinner-border"></div>;
  if (error) return <div className="alert">{error}</div>;

  return (
    <div className="ss">
      <div className="containe">
        <h2 className="heading">Edit Profile</h2>
        <form onSubmit={handleSubmit} className="form">
          {/* First Name */}
          <div className="form-group">
            <label className="form-label">First Name</label>
            <input
              type="text"
              className="input"
              name="firstName"
              value={formData.firstName || ""}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/[^A-Za-z]/g, "");
              }}
            />
            {formErrors.firstName && (
              <div className="error-message">{formErrors.firstName}</div>
            )}
          </div>

          {/* Last Name */}
          <div className="form-group">
            <label className="form-label">Last Name</label>
            <input
              type="text"
              className="input"
              name="lastName"
              value={formData.lastName || ""}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/[^A-Za-z]/g, "");
              }}
            />
            {formErrors.lastName && (
              <div className="error-message">{formErrors.lastName}</div>
            )}
          </div>

          {/* Phone Number */}
          <div className="form-group">
            <label className="form-label">Phone Number</label>
            <input
              type="text"
              className="input"
              name="phoneNumber"
              value={formData.phoneNumber || ""}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, "").slice(0, 8);
              }}
            />
            {formErrors.phoneNumber && (
              <div className="error-message">{formErrors.phoneNumber}</div>
            )}
          </div>

          {/* Address */}
          <div className="form-group">
            <label className="form-label">Address</label>
            <input
              type="text"
              className="input"
              name="adress"
              value={formData.adress || ""}
              onChange={handleChange}
            />
            {formErrors.adress && (
              <div className="error-message">{formErrors.adress}</div>
            )}
          </div>

          {/* CIN */}
          <div className="form-group">
            <label className="form-label">CIN</label>
            <input
              type="text"
              className="input"
              name="cin"
              value={formData.cin || ""}
              onChange={handleChange}
              onInput={(e) => {
                e.target.value = e.target.value.replace(/\D/g, "").slice(0, 8);
              }}
            />
            {formErrors.cin && (
              <div className="error-message">{formErrors.cin}</div>
            )}
          </div>

          {/* Email */}
          <div className="form-group">
            <label className="form-label">Email</label>
            <input
              type="email"
              className="input"
              name="email"
              value={formData.email || ""}
              onChange={handleChange}
            />
            {formErrors.email && (
              <div className="error-message">{formErrors.email}</div>
            )}
          </div>

          {/* Image */}
          <div className="form-group">
            <label className="form-label">Image</label>
            <input
              type="file"
              className="input"
              name="image"
              onChange={handleChange}
            />
          </div>

          <div className="b">
            <button 
              type="submit" 
              className="login-button"
              disabled={!isFormValid()}
            >
              Save
            </button>
            <button
              type="button"
              className="login-button"
              onClick={handleCancel}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateInfosForm;