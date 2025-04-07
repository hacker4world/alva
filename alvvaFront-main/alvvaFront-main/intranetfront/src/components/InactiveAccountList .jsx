import React, { useEffect, useState } from "react";
import axios from "axios";
import "../Style/table.css";

const InactiveAccountList = () => {
  const [inactiveAccounts, setInactiveAccounts] = useState([]);
  const status = "inactive";
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    fetchInactiveAccounts();
  }, [status]);

  const fetchInactiveAccounts = async () => {
    try {
      const token = sessionStorage.getItem("token");

      if (!token) {
        console.error("No authentication token found.");
        alert("You are not authorized. Please log in.");
        return;
      }

      console.log("Fetching inactive accounts with token:", token);

      const response = await axios.get(
        `${apiUrl}/api/authentication/inactiveAccount?status=${status}`,
        {
          headers: { Authorization: token },
        }
      );

      setInactiveAccounts(response.data);
    } catch (error) {
      console.error(
        "Error fetching inactive accounts:",
        error.response?.data || error.message
      );
      alert("Failed to load inactive accounts.");
    }
  };

  const handleActivation = async (email) => {
    try {
      const token = sessionStorage.getItem("token");

      if (!token) {
        console.error("No authentication token found.");
        alert("You are not authorized. Please log in.");
        return;
      }

      console.log("Sending activation email to:", email);

      const response = await axios.post(
        `${apiUrl}/api/authentication/sendActivationEmail?email=${email}`,
        {},
        {
          headers: { Authorization: token },
        }
      );

      console.log("Activation email sent successfully:", response.data);
      alert("Activation email sent successfully!");
    } catch (error) {
      console.error(
        "Error sending activation email:",
        error.response?.data || error.message
      );
      alert("Failed to send activation email.");
    }
  };

  const handleRejection = async (id) => {
    try {
      const token = sessionStorage.getItem("token");

      if (!token) {
        console.error("No authentication token found.");
        alert("You are not authorized. Please log in.");
        return;
      }

      console.log("Rejecting activation request for user ID:", id);

      const response = await axios.delete(
        `${apiUrl}/api/userManagement/rejectActivationRequest/${id}`,
        {
          headers: { Authorization: token },
        }
      );

      console.log("Rejection successful:", response.data);
      alert("User request rejected.");
      fetchInactiveAccounts(); // Refresh list
    } catch (error) {
      console.error(
        "Error rejecting activation request:",
        error.response?.data || error.message
      );
      alert("Failed to reject request.");
    }
  };

  return (
    <div className="container">
      <div className="ss">
        <h1 className="heading">List of inactive accounts</h1>
        <table className="styled-table">
          <thead>
            <tr>
              <th>CIN</th>
              <th>Matricule</th>
              <th>Prénom</th>
              <th>Nom</th>
              <th>Email</th>
              <th>Adresse</th>
              <th>Téléphone</th>
              <th>Action 1</th>
              <th>Action 2</th>
            </tr>
          </thead>
          <tbody>
            {inactiveAccounts.map((user) => (
              <tr key={user.id}>
                <td>{user.cin}</td>
                <td>{user.matriculeNumber}</td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.adress}</td>
                <td>{user.phoneNumber}</td>
                <td>
                  <button
                    className="action-button activate"
                    onClick={() => handleActivation(user.email)}
                  >
                    Activate
                  </button>
                </td>
                <td>
                  <button
                    className="action-button reject"
                    onClick={() => handleRejection(user.id)}
                  >
                    Reject
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default InactiveAccountList;
