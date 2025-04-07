import { useNavigate } from "react-router-dom";

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
 
    sessionStorage.removeItem("token");

 
    navigate("/login");
  };

  return (
    <button onClick={handleLogout} className="btn-logout">
      Déconnexion
    </button>
  );
};

export default Logout;
