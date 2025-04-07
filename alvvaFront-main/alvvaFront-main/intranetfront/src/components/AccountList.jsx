import React, { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import "../styles/table.css";
import { useNavigate } from "react-router-dom";
import Navbar from "../layouts/admin/Navbar";

const AccountList = () => {
  const [accounts, setAccounts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const navigate = useNavigate();
  const status = "active";
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      const token = sessionStorage.getItem("token");
      const activeResponse = await axios.get(`${apiUrl}/api/userManagement/getAllAccounts?status=active`, {
        headers: { Authorization: token },
      });
  
      const blockedResponse = await axios.get(`${apiUrl}/api/userManagement/getAllAccounts?status=blocked`, {
        headers: { Authorization: token },
      });

      const allAccounts = [...activeResponse.data, ...blockedResponse.data];
      setAccounts(allAccounts);

    } catch (error) {
      console.error("Error !!!", error);
    }
  };

  const validateForm = (formData) => {
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

    return errors;
  };

  const handleUpdate = async (user) => {
    const { value: formValues } = await Swal.fire({
      title: "Update User",
      html:
        `<input id='swal-cin' class='swal2-input' placeholder='CIN' value='${user.cin}' maxlength="8">` +
        `<input id='swal-firstName' class='swal2-input' placeholder='First Name' value='${user.firstName}'>` +
        `<input id='swal-lastName' class='swal2-input' placeholder='Last Name' value='${user.lastName}'>` +
        `<input id='swal-phoneNumber' class='swal2-input' placeholder='Phone Number' value='${user.phoneNumber}' maxlength="8">` +
        `<input id='swal-adress' class='swal2-input' placeholder='Address' value='${user.adress}'>` +
        `<input id='swal-email' class='swal2-input' placeholder='Email' value='${user.email}'>`,
      focusConfirm: false,
      showCancelButton: true,
      preConfirm: () => {
        const formData = {
          cin: document.getElementById("swal-cin").value,
          firstName: document.getElementById("swal-firstName").value,
          lastName: document.getElementById("swal-lastName").value,
          phoneNumber: document.getElementById("swal-phoneNumber").value,
          adress: document.getElementById("swal-adress").value,
          email: document.getElementById("swal-email").value,
        };

        const errors = validateForm(formData);


        if (Object.keys(errors).length > 0) {
          let errorMessage = '';
          for (const field in errors) {
            errorMessage += `${errors[field]}\n`;
          }
          Swal.showValidationMessage(errorMessage);
          return false;
        }

        return formData;
      },
      didOpen: () => {
        // Add input event listeners for validation
        const cinInput = document.getElementById('swal-cin');
        const firstNameInput = document.getElementById('swal-firstName');
        const lastNameInput = document.getElementById('swal-lastName');
        const phoneInput = document.getElementById('swal-phoneNumber');

        // Only allow numbers for CIN and phone
        cinInput.addEventListener('input', (e) => {
          e.target.value = e.target.value.replace(/\D/g, '').slice(0, 8);
        });

        phoneInput.addEventListener('input', (e) => {
          e.target.value = e.target.value.replace(/\D/g, '').slice(0, 8);
        });

        // Only allow letters for names
        firstNameInput.addEventListener('input', (e) => {
          e.target.value = e.target.value.replace(/[^A-Za-z]/g, '');
        });

        lastNameInput.addEventListener('input', (e) => {
          e.target.value = e.target.value.replace(/[^A-Za-z]/g, '');
        });
      }
    });

    if (formValues) {
      try {
        const token = sessionStorage.getItem("token");
        await axios.put(
          `${apiUrl}/api/userManagement/updateUser/${user.id}`,
          formValues,
          {
            headers: {
              Authorization: token,
              "Content-Type": "application/json",
            },
          }
        );
        Swal.fire("Updated!", "User information has been updated.", "success");
        fetchAccounts();
      } catch (error) {
        Swal.fire("Error", "Failed to update user information.", "error");
      }
    }
  };

  const handleDelete = async (email, matriculeNumber) => {
    try {
      const { value: userMatricule } = await Swal.fire({
        title: "Confirm Deletion",
        input: "text",
        inputLabel: `Please enter the matricule number: ${matriculeNumber} to confirm deletion`,
        inputPlaceholder: "Enter matricule number",
        showCancelButton: true,
      });

      if (!userMatricule) {
        Swal.fire("Cancelled", "Deletion aborted.", "error");
        return;
      }

      if (userMatricule !== matriculeNumber) {
        Swal.fire(
          "Error",
          "Incorrect matricule number. Deletion aborted.",
          "error"
        );
        return;
      }

      const deleteDto = {
        email: email,
        matriculeNumber: matriculeNumber,
      };

      const token = sessionStorage.getItem("token");
      await axios.delete(`${apiUrl}/api/authentication/deleteAccount`, {
        headers: { Authorization: token },
        data: deleteDto,
      });

      Swal.fire("Deleted!", "The account has been deleted.", "success");
      fetchAccounts();
    } catch (error) {
      Swal.fire(
        "error",
        "User not found or incorrect matricule number.",
        "error"
      );
    }
  };

  const handleUnblock = async (id) => {
    const confirmResult = await Swal.fire({
      title: "Are you sure?",
      text: "Do you want to unblock this user?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, unblock!",
    });

    if (confirmResult.isConfirmed) {
      try {
        const token = sessionStorage.getItem("token");
        await axios.post(`${apiUrl}/api/userManagement/unblockUser?id=${id}`, {}, {
          headers: { Authorization: token },
        });

        Swal.fire("Success", "User unblocked successfully!", "success");
        fetchAccounts(); 
      } catch (error) {
        Swal.fire("Error", "Failed to unblock user.", "error");
      }
    }
  };

  const filteredAccounts = accounts.filter((user) =>
    user.matriculeNumber.includes(searchTerm)
  );

  return (
    <div>
      <Navbar />
      <div className="contain">
        <div className="ll">
          <h1 className="heading">Accounts List</h1>
          <input
            type="text"
            placeholder="Search by Matricule"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-bar"
          />
          <table className="styled-table">
            <thead>
              <tr>
                <th>CIN</th>
                <th>Matricule</th>
                <th>Firstname</th>
                <th>Lastname</th>
                <th>Email</th>
                <th>Address</th>
                <th>Phone</th>
                <th>Action 1</th>
                <th>Action 2</th>
              </tr>
            </thead>
            <tbody>
              {filteredAccounts.map((user) => (
                <tr key={user.id} className={user.status === "blocked" ? "blocked-user" : ""}>
                  <td>{user.cin}</td>
                  <td>{user.matriculeNumber}</td>
                  <td>{user.firstName}</td>
                  <td>{user.lastName}</td>
                  <td>{user.email}</td>
                  <td>{user.adress}</td>
                  <td>{user.phoneNumber}</td>
                  <td>
                  {user.status === "blocked" ? (
                      <button
                        className="action-button unblock blocked-style"
                        onClick={() => handleUnblock(user.id)}
                      >
                        🚫 Blocked
                      </button>
                    ) : (
                    <button
                      className="action-button activate"
                      onClick={() => handleUpdate(user)}
                    >
                      update
                    </button>
                       )}
                  </td>
                  <td>
                    <button
                      className="action-button reject"
                      onClick={() =>
                        handleDelete(user.email, user.matriculeNumber)
                      }
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AccountList;