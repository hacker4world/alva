import React from "react";
import EditPersoInfos from "./pages/EditPersoInfos";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ForgotPassword from "./components/ForgotPassword";
import DisplayPersoInfos from "./components/DisplayPersoInfos";
import UpdateOldPassword from "./components/UpdateOldPassword";
import CreateUserAccount from "./components/CreateUserAccount";
import Admin from "./pages/Admin";
import Navbar from "./layouts/admin/Navbar";
import Logout from "./components/Logout";

import NavbarE from "./layouts/Employee/NavbarE";
import NavbarM from "./layouts/Manager/NavbarM";
import NavbarW from "./layouts/worker/NavbarW";
import CreateUserForm from "./components/CreateUserAccount";

import WelcomePage from "./pages/welcomePage";
import WaitingPage from "./pages/WaitingPage";
import AccountList from "./components/AccountList";
import CreatePost from "./components/PostManagement/CreatePost";
import UpdatePost from "./components/PostManagement/UpdatePost";
import PostsByDepartment from "./components/PostsByDepartment";
import AllPosts from "./components/AllPosts";




function App() {
  return (
    <div>
      <Router>
        <Routes>
        <Route path="/AllPosts" element={<AllPosts/>}/>
          <Route path="/PostsByDepartment" element={<PostsByDepartment/> }/>
          <Route path="/waitingPage" element={<WaitingPage />} />
          <Route path="/NavbarW" element={<NavbarW />} />
          <Route path="/NavbarM" element={<NavbarM />} />
          <Route path="/NavbarE" element={<NavbarE />} />
          <Route path="/createUser" element={<CreateUserForm />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/nav" element={<Navbar />} />
          <Route path="/admin-dashboard/*" element={<Admin />} />
          <Route path="/userProfile" element={<DisplayPersoInfos />} />
          <Route path="/updateInfos" element={<EditPersoInfos />} />
          <Route path="/forgetPassword" element={<ForgotPassword />} />
          <Route path="/updateOldPassword" element={<UpdateOldPassword />} />
          <Route path="/login" element={<WelcomePage />} />
          <Route path="/AccountsList" element={<AccountList />} />
          <Route path="/createUserAccount" element={<CreateUserAccount />} />
          <Route path="/createPost" element={<CreatePost />} />
          <Route path="/updatePost/:id" element={<UpdatePost />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
