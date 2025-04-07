import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Swal from "sweetalert2";
import "../../styles/PostManagement/CreatePost.css";

const CreatePost = () => {
  const [postDto, setPostDto] = useState({
    title: "",
    description: "",
    content: "",
    category: "",
    attachment: null,
    user_id: "",
  });

  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [isModalOpen, setIsModalOpen] = useState(false);
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const id = sessionStorage.getItem("userId");
    if (id) {
      setPostDto(prev => ({ ...prev, user_id: id }));
    }
  }, []);

  const toggleModal = () => setIsModalOpen(!isModalOpen);

  const validateForm = () => {
    const newErrors = {};
    if (!postDto.title.trim()) newErrors.title = "Title is required";
    if (!postDto.description.trim()) newErrors.description = "Description is required";
    if (!postDto.content.trim()) newErrors.content = "Content is required";
    if (!postDto.category) newErrors.category = "Category is required";
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    setPostDto(prev => ({
      ...prev,
      [name]: name === "attachment" ? files[0] : value,
    }));
    
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: "" }));
    }
  };

  const isFormValid = () => {
    return (
      postDto.title.trim() && 
      postDto.description.trim() && 
      postDto.content.trim() && 
      postDto.category &&
      Object.values(errors).every(error => !error)
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const postResponse = await axios.post(`${apiUrl}/posts/addPost`, {
        title: postDto.title,
        description: postDto.description,
        content: postDto.content,
        category: postDto.category,
        user_id: postDto.user_id,
        attachment: null 
      });

      if (!postResponse.data || postResponse.data.pub_id === undefined) {
        throw new Error("Failed to get post ID from server");
      }

      const postId = postResponse.data.pub_id;

      if (postDto.attachment) {
        const formData = new FormData();
        formData.append("attachment", postDto.attachment);

        await axios.post(
          `${apiUrl}/posts/${postId}/addAttachment`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
      }

      Swal.fire("Posted!", "Your post has been created!", "success");
      resetForm();
      navigate("/admin-dashboard");
    } catch (error) {
      console.error("Error:", error);
      Swal.fire({
        icon: "error",
        title: "Error Creating Post",
        text: error.response?.data?.message || error.message || "There was an error creating the post",
      });
    }
  };

  const resetForm = () => {
    setPostDto({
      title: "",
      description: "",
      content: "",
      category: "",
      attachment: null,
      user_id: postDto.user_id
    });
    setErrors({});
  };

  return (
    <div className="post-container">
      <div className="post-prompt" onClick={toggleModal}>
        <input 
          type="text" 
          placeholder="What's new?" 
          className="post-input-trigger"
          readOnly
        />
      </div>
      
      {isModalOpen && (
        <div className="modal-overlay">
          <div className="create-post-modal">
            <div className="modal-header">
              <h2>Create Post</h2>
              <button 
                onClick={() => {
                  resetForm();
                  toggleModal();
                }} 
                className="close-btn"
              >
                &times;
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="post-form">
              <input
                type="text"
                name="title"
                value={postDto.title}
                onChange={handleChange}
                className={`post-title-input ${errors.title ? 'error-border' : ''}`}
                placeholder="Title"
                required
              />
              {errors.title && <div className="error-message">{errors.title}</div>}

              <input
                type="text"
                name="description"
                value={postDto.description}
                onChange={handleChange}
                className={`post-desc-input ${errors.description ? 'error-border' : ''}`}
                placeholder="Description"
                required
              />
              {errors.description && <div className="error-message">{errors.description}</div>}

              <select
                name="category"
                value={postDto.category}
                className={`category-select ${errors.category ? 'error-border' : ''}`}
                onChange={handleChange}
                required
              >
                <option value="">Select Category</option>
                <option value="Production">Production</option>
                <option value="Quality">Quality</option>
                <option value="RH">RH</option>
                <option value="IT">IT</option>
              </select>
              {errors.category && <div className="error-message">{errors.category}</div>}
              
              <textarea
                name="content"
                value={postDto.content}
                onChange={handleChange}
                placeholder="Content"
                className={`post-content-input ${errors.content ? 'error-border' : ''}`}
                required
                rows="5"
              />
              {errors.content && <div className="error-message">{errors.content}</div>}

              <div className="attachment-options">
                <label className="attachment-label">
                  <input
                    type="file"
                    className="file-input"
                    name="attachment"
                    onChange={handleChange}
                  />
                  <span className="attachment-icon">📎</span>
                  <span>Add to your post</span>
                </label>
                {postDto.attachment && (
                  <span className="file-name">{postDto.attachment.name}</span>
                )}
              </div>
              
              <button 
                type="submit" 
                className="post-submit-btn"
                disabled={!isFormValid()}
              >
                Create Post
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CreatePost;