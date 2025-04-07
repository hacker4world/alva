import React, { useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import "../../styles/PostManagement/updatePost.css";
import Swal from "sweetalert2";

const UpdatePost = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    content: "",
    category: "",
    attachment_path: "",
    attachment: null,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const location = useLocation();
  const apiUrl = import.meta.env.VITE_API_URL;

   
  const handleChange = (e) => {
    const { name, value, files } = e.target;

    setFormData((prevData) => ({
      ...prevData,
      [name]: name === "attachment" ? files[0] : value,
    }));
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${apiUrl}/posts/updatePost/${pub_id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ ...formData, attachment: undefined }),
      });

      if (!response.ok) throw new Error("Update failed");

      if (formData.attachment) {
        const imageFormData = new FormData();
        imageFormData.append("attachment", formData.attachment);

        await fetch(`${apiUrl}/posts/${pub_id}/updateAttachment`, {
          method: "PUT",
          body: imageFormData,
        });
      }
    
      
      Swal.fire({
        icon: "success",
        title: "Post Updating Successful!",
        text: "Post Updated Successfully!!",
      })

    } catch (error) {
      setError(error.message);
    }
  };

  if (loading) return <div className="spinner-border"></div>;
  if (error) return <div className="alert">{error}</div>;

  return (
    <div className="ss">
      <div className="containe">
        <h2 className="heading">Edit Post</h2>
        <form onSubmit={handleSubmit} className="form">
      
          <div className="form-group">
            <label className="form-label"> Title </label>
            <input
              type="text"
              className="input"
              name="title"
              value={formData.title || ""}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Description</label>
            <input
              type="text"
              className="input"
              name="description"
              value={formData.description || ""}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Content</label>
            <input
              type="text"
              className="input"
              name="content"
              value={formData.content || ""}
              onChange={handleChange}
            />
          </div>
   
          <div className="form-group">
            <label className="form-label">Attachment</label>
            <input
              type="file"
              className="input"
              name="attachment"
              onChange={handleChange}
            />
          </div>

          <div className="b">
            <button 
              type="submit" 
              className="login-button"
            >
              Save
            </button>
            <button
              type="button"
              className="login-button"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdatePost;