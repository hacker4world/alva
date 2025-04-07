import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaThumbsUp, FaComment, FaShare, FaEllipsisV } from "react-icons/fa";
import Swal from "sweetalert2";
import "../styles/post.css";
import NavbarE from "../layouts/Employee/NavbarE";

const AllPosts = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [menuOpen, setMenuOpen] = useState(null);

  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get(`${apiUrl}/posts/non-archived`);
        if (Array.isArray(response.data)) {
          setPosts(response.data);
        } else {
          setError("No non-archived posts found");
        }
      } catch (err) {
        setError("Error loading posts.");
      } finally {
        setLoading(false);
      }
    };
    fetchPosts();
  }, []);

  const handleDelete = async (postId) => {
    try {
      await axios.delete(`${apiUrl}/posts/delete/${postId}`);
      setPosts(posts.filter((post) => post.pub_id !== postId));
    } catch (err) {
      console.error("Error deleting post:", err);
    }
  };

  const handleArchive = async (postId) => {
    try {
      await axios.put(`${apiUrl}/posts/archivePost/${postId}`);
      setPosts(posts.filter((post) => post.pub_id !== postId));
      Swal.fire("Archived!", "Post has been archived.", "success");
    } catch (err) {
      Swal.fire("Error", "Failed to archive the post.", "error");
    }
  };

  const handleUpdate = async (post) => {
    const { value: formValues } = await Swal.fire({
      title: "Edit Post",
      html: `
        <input id="swal-title" class="swal2-input" placeholder="Title" value="${
          post.title || ""
        }" />
        <input id="swal-description" class="swal2-input" placeholder="Description" value="${
          post.description || ""
        }" />
        <textarea id="swal-content" class="swal2-textarea" placeholder="Content">${
          post.content || ""
        }</textarea>
        <input id="swal-file" type="file" class="swal2-file" />
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: "Save",
      preConfirm: () => {
        return {
          title: document.getElementById("swal-title").value,
          description: document.getElementById("swal-description").value,
          content: document.getElementById("swal-content").value,
          file: document.getElementById("swal-file").files[0] || null,
        };
      },
    });

    if (formValues) {
      try {
        await axios.put(`${apiUrl}/posts/updatePost/${post.pub_id}`, {
          title: formValues.title,
          description: formValues.description,
          content: formValues.content,
        });

        if (formValues.file) {
          const formData = new FormData();
          formData.append("attachment", formValues.file);
          await axios.put(
            `${apiUrl}/posts/${post.pub_id}/updateAttachment`,
            formData
          );
        }

        setPosts((prev) =>
          prev.map((p) =>
            p.pub_id === post.pub_id
              ? {
                  ...p,
                  title: formValues.title,
                  description: formValues.description,
                  content: formValues.content,
                  attachment: formValues.file ? "" : p.attachment,
                }
              : p
          )
        );

        Swal.fire("Success", "Post updated successfully!", "success");
      } catch (err) {
        console.error(err);
        Swal.fire("Error", "Failed to update the post.", "error");
      }
    }
  };

  if (loading)
    return (
      <div className="loading-spinner">
        <div className="spinner"></div>
      </div>
    );
  if (error)
    return (
      <div className="error-message">
        <p>{error}</p>
      </div>
    );

  return (
    <div className="nav">
      <NavbarE />
      <div className="posts-container">
        <h2 className="department-title">Posts</h2>
        {posts.length === 0 ? (
          <div className="no-posts">
            <p>No posts found.</p>
          </div>
        ) : (
          <div className="posts-grid">
            {posts.map((post) => (
              <div key={post.pub_id} className="post-card">
                <div className="post-header">
                  <div className="user-avatar">
                    {post.user?.image ? (
                      <img
                        src={`data:image/jpeg;base64,${post.user.image}`}
                        alt="User Avatar"
                        className="avatar-image"
                      />
                    ) : (
                      <span className="avatar-placeholder">
                        {post.user?.firstName?.charAt(0).toUpperCase() || "U"}
                      </span>
                    )}
                  </div>

                  <div className="user-info">
                    <h3>
                      {post.user?.firstName}{" "}
                      {post.user?.lastName || "Unknown Author"}
                    </h3>
                    <p className="post-date">
                      {new Date(post.publication_date).toLocaleString("en-US", {
                        weekday: "long",
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </p>
                  </div>

                  <div className="post-menu">
                    <FaEllipsisV
                      className="menu-icon"
                      onClick={() =>
                        setMenuOpen(
                          menuOpen === post.pub_id ? null : post.pub_id
                        )
                      }
                    />
                    {menuOpen === post.pub_id && (
                      <ul className="menu-dropdown">
                        <li onClick={() => handleDelete(post.pub_id)}>
                          Delete
                        </li>
                        <li onClick={() => handleArchive(post.pub_id)}>
                          Archive
                        </li>
                        <li onClick={() => handleUpdate(post)}>Edit</li>
                      </ul>
                    )}
                  </div>
                </div>

                <div className="post-content">
                  <h3 className="post-title">{post.title}</h3>
                  <p className="post-description">{post.content}</p>
                  <p className="post-description">{post.description}</p>
                </div>

                {(post.image || post.attachment) && (
                  <div className="post-image">
                    <img
                      src={
                        post.image
                          ? `${apiUrl}/uploads/${post.image}`
                          : `data:image/jpeg;base64,${post.attachment}`
                      }
                      alt={post.title}
                      onError={(e) => (e.target.style.display = "none")}
                    />
                  </div>
                )}

                <div className="post-stats">
                  <span>120 Likes</span>
                  <span>15 Comments</span>
                </div>

                <div className="post-actions">
                  <button className="action-btn">
                    <FaThumbsUp className="icon" /> Like
                  </button>
                  <button className="action-btn">
                    <FaComment className="icon" /> Comment
                  </button>
                  <button className="action-btn">
                    <FaShare className="icon" /> Share
                  </button>
                </div>

                <div className="comment-section">
                  <div className="comment-input-container">
                    <input
                      type="text"
                      placeholder="Write a comment..."
                      className="comment-input"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AllPosts;
