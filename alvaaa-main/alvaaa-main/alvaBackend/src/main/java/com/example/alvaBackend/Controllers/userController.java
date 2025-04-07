package com.example.alvaBackend.Controllers;


import com.example.alvaBackend.Dto.*;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.userRepository;
import com.example.alvaBackend.Security.JwtService;
import com.example.alvaBackend.Services.emailServices;
import com.example.alvaBackend.Services.userService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/userManagement")
public class userController {

    private final userService userService;
    private final userRepository userRepository;
    private final JwtService jwtService;
    private final emailServices emailService;

    public userController(userService userService, userRepository userRepository, JwtService jwtService, emailServices emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    // --- CreateAccountForUsers ---
    @PostMapping("/createUserAccount")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        return userService.createUserAccount(userDto);
    }

    // --- UpdateUserInfos ---
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UpdateUserDto updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }
    // --- Image Management ---
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<?> addImage(@PathVariable int id, @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(userService.addImage(id, image));
    }

    @PutMapping("/{id}/updateImage")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestParam(value = "image", required = false)  MultipartFile image) {
        return ResponseEntity.ok(userService.updateImage(id, image));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getUserImage(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserImage(id));
    }

    // --- ResetPassword ---
    @PostMapping("/resetPassword/requestCode")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            userService.sendPasswordResetCode(resetPasswordDto.getEmail());
            return ResponseEntity.ok("Password reset code has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending reset code: " + e.getMessage());
        }
    }

    @PostMapping("/resetPassword/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody  ResetPasswordDto resetPasswordDto) {
        boolean isCodeValid = userService.verifyResetCode(resetPasswordDto.getEmail(), resetPasswordDto.getCode());
        if (isCodeValid) {
            return ResponseEntity.ok("Code verified successfully. You can now reset your password.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code.");
        }

    }

    @PutMapping("/resetPassword/updatePassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }

    @PutMapping("/resetPassword/updateOldPassword")
    public ResponseEntity<String> resetOldPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetOldPassword(resetPasswordDto);
    }


    @GetMapping("/getUserbyId/{id}")
    public ResponseEntity<?> getUserbyId(@PathVariable("id") int id ){
        return userService.getUserbyId(id);
    }



    // --- RejectActivationRequest ---
    @DeleteMapping("/rejectActivationRequest/{id}")
    public ResponseEntity<String> rejectRequest(@PathVariable int id) {
        return userService.rejectRequest(id);
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<User>> getAllAccounts(@RequestParam String status) {
        return userService.getAllAccounts(status);
    }

    @PostMapping("/unblockUser")
    public ResponseEntity<String> unblockUser(@RequestParam int id) {
        return userService.unblockAccount(id);
    }

}


