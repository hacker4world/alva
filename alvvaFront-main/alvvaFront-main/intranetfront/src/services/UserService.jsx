import React,{axios} from 'react'


const API_URL = "http://localhost:8081/api/userManagement"; 

const UserService = {

    // --- CreateAccountForUsers ---
    createUserAccount: async (userDto) => {
        try {
            const response = await axios.post(`${API_URL}/createUserAccount`, userDto);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error creating user account');
        }
    },

    // --- UserManagement ---
    updateUser: async (id, updatedUser) => {
        try {
            const response = await axios.put(`${API_URL}/updateUser/${id}`, updatedUser);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error updating user');
        }
    },


    // --- Image Management ---

    // Upload user image
    uploadImage: async (id, image) => {
        const formData = new FormData();
        formData.append('image', image);
        
        try {
            const response = await axios.post(`${API_URL}/${id}/uploadImage`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error uploading image');
        }
    },

    // Update user image
    updateImage: async (id, image) => {
        const formData = new FormData();
        formData.append('image', image);

        try {
            const response = await axios.put(`${API_URL}/${id}/updateImage`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error updating image');
        }
    },

    // Get user image
    getUserImage: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/${id}/image`);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error fetching user image');
        }
    },



    // --- ResetPassword ---
    // Request password reset code
    requestPasswordReset: async (email) => {
        try {
            const response = await axios.post(`${API_URL}/resetPassword/requestCode`, { email });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error requesting password reset');
        }
    },

    // Verify password reset code
    verifyResetCode: async (email, code) => {
        try {
            const response = await axios.post(`${API_URL}/resetPassword/verifyCode`, { email, code });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error verifying reset code');
        }
    },

    // Reset password
    resetPassword: async (email, newPassword, confirmPassword) => {
        try {
            const response = await axios.put(`${API_URL}/resetPassword/updatePassword`, {
                email,     
                newPassword,        
                confirmPassword,  
            });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error resetting password');
        }
    },

    // --- RejectActivationRequest ---
    rejectActivationRequest: async (accountDto) => {
        try {
            const response = await axios.delete(`${API_URL}/rejectActivationRequest`, { data: accountDto });
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data || 'Error rejecting activation request');
        }
    }

   
  
}

export default UserService