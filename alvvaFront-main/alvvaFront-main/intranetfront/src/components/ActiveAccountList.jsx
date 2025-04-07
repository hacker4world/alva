import React, { useEffect, useState } from "react";
import axios from "axios";
import "../Style/table.css";
import { useNavigate } from "react-router-dom";

const ActiveAccountList = () => {
  const [activeAccounts, setActiveAccounts] = useState([]);
  const navigate = useNavigate();
  const status = "active";
   const apiUrl = import.meta.env.VITE_API_URL;


  useEffect(() => {
    fetchActiveAccounts();
  }, [status]);

  const fetchActiveAccounts = async () => {
    try {
      const token = sessionStorage.getItem("token");
      const response = await axios.get(
        `${apiUrl}/api/authentication/inactiveAccount?status=${status}`,
        {
          headers: { Authorization: token },
        }
      );
      console.log(token);

      setActiveAccounts(response.data);
    } catch (error) {
      console.error(
        "Erreur lors de la récupération des comptes actives",
        error
      );
    }
  };

  const handleUpdate = (userId) => {
    navigate(`/updateUser/${userId}`);
  };
  const handleDelete = async (email, matriculeNumber) => {
    try {
      const userMatricule = prompt(
        `Please enter the matricule number :${matriculeNumber} to confirm deletion:`
      );

      if (!userMatricule) {
        alert("Deletion cancelled.");
        return;
      }

      if (userMatricule !== matriculeNumber) {
        alert("Incorrect matricule number. Deletion aborted.");
        return;
      }

      const deleteDto = {
        email: email,
        matriculeNumber: matriculeNumber,
      };
      console.log("Sending DELETE request with:", deleteDto);

      const token = sessionStorage.getItem("token");
      console.log("Token:", token);
      console.log("Email:", email);
      console.log("Matricule Number:", matriculeNumber);

      const response = await axios.delete(
        `${apiUrl}/api/authentication/deleteAccount`,
        {
          headers: { Authorization: token },
          data: deleteDto,
        }
      );

      console.log("Account deleted successfully:", response.data);

      fetchActiveAccounts();
    } catch (error) {
      console.error("User not found or Incorrect matricule number", error);
    }
  };

  return (
    <div className="container">
      <div className="ss">
        <h1 className="heading">List of active accounts</h1>
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
            {activeAccounts.map((user) => (
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
                    onClick={() => handleUpdate(user.id)}
                  >
                    update
                  </button>
                </td>
                <td>
                  <button
                    className="action-button reject"
                    onClick={() =>
                      handleDelete(user.email, user.matriculeNumber)
                    }
                  >
                    delete
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

export default ActiveAccountList;
