import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import UpdateOldPassword from "../components/UpdateOldPassword";
import CreateUserForm from "../components/CreateUserForm";
import EditPersoInfos from "./EditPersoInfos";
import Navbar from "../layouts/admin/Navbar";
import DisplayPersoInfos from "../components/DisplayPersoInfos";
import AccountList from "../components/AccountList";
import CreatePost from "../components/PostManagement/CreatePost";
import UpdatePost from "../components/PostManagement/UpdatePost";


function Admin() {
  return (
    <div>
  
        <Navbar />
        <Routes>
          <Route path="/updateUser/:id" element={<EditPersoInfos />} />
          <Route path="/userProfile" element={<DisplayPersoInfos />} />
          <Route path="/updateOldPassword" element={<UpdateOldPassword />} />
          <Route path="/create-user" element={<CreateUserForm />} />
          <Route path="/AccountsList" element={<AccountList />} />
          <Route path="/createPost" element={<CreatePost/>} />
          <Route path="/updatePost/:id" element={<UpdatePost />} />      
        </Routes>
    
    </div>
  );
}

export default Admin;
