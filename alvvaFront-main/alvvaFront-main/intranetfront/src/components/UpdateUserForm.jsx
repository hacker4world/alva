import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "../styles/updateUser.css";

const UpdateUserForm = () => {
  const { id } = useParams();
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
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const token = sessionStorage.getItem("token");
        const response = await fetch(
          `${apiUrl}/api/userManagement/getUserbyId/${id}`,
          {
            headers: { Authorization: token },
          }
        );
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
  }, [id]);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: name === "image" ? files[0] : value,
    }));
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.firstName) errors.firstName = "First Name is required";
    if (!formData.lastName) errors.lastName = "Last Name is required";
    if (!formData.phoneNumber || !/\d{8}/.test(formData.phoneNumber)) {
      errors.phoneNumber = "Phone number must be 8 digits";
    }
    if (!formData.adress) errors.adress = "Address is required";
    if (!formData.cin || !/\d{8}/.test(formData.cin)) {
      errors.cin = "CIN must be 8 digits";
    }
    if (!formData.email || !/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = "Invalid email";
    }
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const token = sessionStorage.getItem("token");
      const response = await fetch(
        `${apiUrl}/api/userManagement/updateUser/${id}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json", Authorization: token },
          body: JSON.stringify({ ...formData, image: undefined }),
        }
      );

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

      alert("User updated successfully");
      navigate(`/ActiveAccountList`);
    } catch (error) {
      setError(error.message);
    }
  };

  if (loading) return <div className="spinner-border"></div>;
  if (error) return <div className="alert">{error}</div>;

  return (
    <div>
      <h2>Edit Profile</h2>
      <div className="container">
        <form onSubmit={handleSubmit}>
          {[
            "firstName",
            "lastName",
            "phoneNumber",
            "adress",
            "cin",
            "email",
          ].map((field) => (
            <div key={field} className="form-group">
              <label className="form-label">
                {field.charAt(0).toUpperCase() + field.slice(1)}
              </label>
              <input
                type="text"
                className="form-control"
                name={field}
                value={formData[field] || ""}
                onChange={handleChange}
              />
              {formErrors[field] && (
                <div className="alert">{formErrors[field]}</div>
              )}
            </div>
          ))}
          <div className="form-group">
            <label className="form-label">Image</label>
            <input
              type="file"
              className="form-control"
              name="image"
              onChange={handleChange}
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Save
          </button>
          <button
            type="button"
            className="btn btn-secondary mt-2"
            onClick={() => navigate(`/ActiveAccountList`)}
          >
            Cancel
          </button>
        </form>
      </div>
    </div>
  );
};

export default UpdateUserForm;
