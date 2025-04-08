import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaThumbsUp, FaComment, FaShare, FaEllipsisV } from "react-icons/fa";
import "../styles/post.css";
import NavbarE from "../layouts/Employee/NavbarE";

const PostsByDepartment = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [departmentId, setDepartmentId] = useState(null);
  const [departmentName, setDepartmentName] = useState("");
  const [menuOpen, setMenuOpen] = useState(null); // Gérer le menu contextuel
  const [commentInput, setCommentInput] = useState(""); // Gérer
  const [commentReplies, setCommentReplies] = useState([]);
  const [replyInput, setReplyInput] = useState("");

  const apiUrl = import.meta.env.VITE_API_URL;
  const userId = sessionStorage.getItem("userId");

  useEffect(() => {
    const fetchUserDepartment = async () => {
      try {
        const response = await axios.get(
          `${apiUrl}/api/userManagement/getUserbyId/${userId}`
        );
        const dept = response.data.department;
        setDepartmentId(dept.department_id);
        setDepartmentName(dept.name || `Département ${dept.department_id}`);
      } catch (err) {
        setError("Erreur lors de la récupération du département.");
        setLoading(false);
      }
    };

    if (userId) fetchUserDepartment();
  }, [userId]);

  useEffect(() => {
    const fetchPosts = async () => {
      if (!departmentId) return;
      try {
        const response = await axios.get(
          `${apiUrl}/posts/byDepartment/${departmentId}`
        );
        console.log(response.data);

        setPosts(response.data);
      } catch (err) {
        setError("Erreur lors du chargement des posts.");
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, [departmentId]);

  const handleDelete = async (postId) => {
    try {
      await axios.delete(`${apiUrl}/posts/delete/${postId}`);
      setPosts(posts.filter((post) => post.pub_id !== postId));
    } catch (err) {
      console.error("Erreur lors de la suppression du post:", err);
    }
  };

  const handleArchive = async (postId) => {
    try {
      await axios.put(`${apiUrl}/posts/archive/${postId}`);
      alert("Post archivé !");
    } catch (err) {
      console.error("Erreur lors de l'archivage du post:", err);
    }
  };

  const handleUpdate = (postId) => {
    alert(`Modifier le post ${postId}`);
  };

  const submitComment = (postId) => {
    const commentData = {
      content: commentInput,
      userId: Number(sessionStorage.getItem("userId")),
      postId: Number(postId),
      parentId: 0,
    };

    console.log(commentData);

    axios
      .post(`${apiUrl}/api/comments/createComment`, commentData)
      .then((response) => {
        console.log(response.data);

        setPosts((old) =>
          old.map((post) => {
            if (post.pub_id == postId)
              return {
                ...post,
                comments: [...post.comments, response.data.body],
              };
            return post;
          })
        );
      })
      .catch(console.log);
  };

  if (loading)
    return (
      <div className="loading-spinner">
        <div className="spinner"></div>
      </div>
    );

  const fetchReplies = (commentId) => {
    axios
      .get(`${apiUrl}/api/comments/${commentId}/replies`)
      .then((response) => {
        setCommentReplies((old) => [...old, ...response.data]);
        console.log(response.data);
      });
  };

  const replyToComment = (commentId) => {
    const data = {
      parentCommentId: commentId,
      userId: Number(sessionStorage.getItem("userId")),
      content: replyInput,
    };

    axios.post(`${apiUrl}/api/comments/reply`, data).then((response) => {
      setCommentReplies((old) => [...old, response.data]);
    });
  };

  if (error) return <p className="error-message">{error}</p>;

  return (
    <div className="nav">
      <NavbarE />
      <div className="posts-container">
        <h2 className="department-title">Publications du {departmentName}</h2>

        {posts.length === 0 ? (
          <div className="no-posts">
            <p>Aucune publication trouvée.</p>
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
                      post.user?.firstName?.charAt(0).toUpperCase() || "U"
                    )}
                  </div>

                  <div className="user-info">
                    <h3>
                      {post.user?.firstName}{" "}
                      {post.user?.lastName || "Auteur inconnu"}
                    </h3>
                    <p className="post-date">
                      {new Date(post.publication_date).toLocaleDateString(
                        "fr-FR",
                        {
                          weekday: "long",
                          year: "numeric",
                          month: "long",
                          day: "numeric",
                          hour: "2-digit",
                          minute: "2-digit",
                        }
                      )}
                    </p>
                  </div>

                  <div className="post-menu">
                    <FaEllipsisV
                      className="menu-icon"
                      onClick={() => setMenuOpen(post.pub_id)}
                    />

                    {menuOpen === post.pub_id && (
                      <ul className="menu-dropdown">
                        <li onClick={() => handleDelete(post.pub_id)}>
                          Delete
                        </li>
                        <li onClick={() => handleArchive(post.pub_id)}>
                          Archiver
                        </li>
                        <li onClick={() => handleUpdate(post.pub_id)}>
                          Update
                        </li>
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
                  <span>120 mentions J'aime</span>
                  <span>15 commentaires</span>
                </div>

                <div className="post-actions">
                  <button className="action-btn">
                    <FaThumbsUp className="icon" /> J'aime
                  </button>
                  <button className="action-btn">
                    <FaComment className="icon" /> Commenter
                  </button>
                  <button className="action-btn">
                    <FaShare className="icon" /> Partager
                  </button>
                </div>

                <div className="comment-section">
                  {post.comments.map((comment) => (
                    <div>
                      <p>{comment.content}</p>
                      <form
                        style={{ width: "100%" }}
                        onSubmit={(e) => {
                          e.preventDefault();
                          replyToComment(comment.id);
                        }}
                      >
                        <input
                          onChange={(e) => {
                            setReplyInput(e.target.value);
                          }}
                          style={{ width: "95%" }}
                          type="text"
                          placeholder="Écrire une response..."
                          className="comment-input"
                        />
                      </form>
                      <br />
                      <button onClick={() => fetchReplies(comment.id)}>
                        see replies
                      </button>

                      {commentReplies
                        .filter((reply) => reply.commentId == comment.id)
                        .map((reply) => (
                          <div>
                            <p>{reply.content}</p>
                          </div>
                        ))}
                    </div>
                  ))}

                  <br />

                  <div className="comment-input-container">
                    <div className="comment-avatar"></div>
                    <form
                      style={{ width: "100%" }}
                      onSubmit={(e) => {
                        e.preventDefault();
                        submitComment(post.pub_id);
                      }}
                    >
                      <input
                        style={{ width: "95%" }}
                        type="text"
                        placeholder="Écrire un commentaire..."
                        className="comment-input"
                        onChange={(e) => {
                          setCommentInput(e.target.value);
                        }}
                      />
                    </form>
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

export default PostsByDepartment;
