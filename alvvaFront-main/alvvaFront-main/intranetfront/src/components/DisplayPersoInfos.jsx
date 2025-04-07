import React, { useEffect, useState } from "react";

import axios from "axios";

function DisplayPersoInfos() {

  const [profileData, setProfileData] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const apiUrl = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const id = sessionStorage.getItem("userId");

    const fetchData = async () => {
      try {
        setLoading(true);

        const token = sessionStorage.getItem("token");
        console.log(token);

        axios
          .get(`${apiUrl}/api/userManagement/getUserbyId/${id}`, {
            headers: { Authorization: token },
          })
          .then((response) => {
            console.log(response.data);

            setProfileData(response.data);
          })
          .catch((err) => {
            setError("failed to load user data");
          })
          .finally(() => setLoading(false));
      } catch (error) {
        setError(error.message);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }


  const {
    matriculeNumber,
    firstName,
    lastName,
    email,
    phoneNumber,
    cin,
    adress,
    image,
  } = profileData;

  return (
    <div>
      <h2>User Details</h2>

      {image && (
        <img
          src={`data:image/jpeg;base64,${image}`}
          alt="user image"
          style={{ width: "100px", height: "100px" }}
        />
      )}


      <p>
        <strong>Matricule Number:</strong> {matriculeNumber || "N/A"}
      </p>
      <p>
        <strong>First Name:</strong> {firstName || "N/A"}
      </p>
      <p>
        <strong>Last Name:</strong> {lastName || "N/A"}
      </p>
      <p>
        <strong>Email:</strong> {email || "N/A"}
      </p>
      <p>
        <strong>Phone Number:</strong> {phoneNumber || "N/A"}
      </p>
      <p>
        <strong>CIN:</strong> {cin || "N/A"}
      </p>
      <p>
        <strong>Address:</strong> {adress || "N/A"}
      </p>
    </div>
  );
}

export default DisplayPersoInfos;
