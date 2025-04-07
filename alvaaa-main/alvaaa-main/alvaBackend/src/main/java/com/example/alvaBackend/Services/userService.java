package com.example.alvaBackend.Services;

import com.example.alvaBackend.Dto.*;
import com.example.alvaBackend.Entities.*;
import com.example.alvaBackend.Repositories.*;

import com.example.alvaBackend.Security.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class userService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final emailServices emailService;
    private final workerRepository workerRepository;
    private final managerRepository managerRepository;
    private final employeeRepository employeeRepository;
    private final JwtService jwtService;
    private final loginAttemptService loginAttemptService;
    private final DepartmentRepository departmentRepository;


    public userService(userRepository userRepository, PasswordEncoder passwordEncoder, emailServices emailService,
                       workerRepository workerRepository, employeeRepository employeeRepository, managerRepository managerRepository,
                       JwtService jwtService, loginAttemptService loginAttemptService, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.workerRepository = workerRepository;
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
        this.departmentRepository = departmentRepository;

    }

    // --- CreateAccountForUsers ---
    public ResponseEntity<String> createUserAccount(UserDto userDto) {

        Optional<User> u = userRepository.findByEmailOrMatriculeNumberOrCin(userDto.getEmail(), userDto.getMatriculeNumber(), userDto.getCIN());

        if (u.isPresent()) {
            return ResponseEntity.status(400).body("User already has existed email or matricule or cin");
        }

        String generatedPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        String accountStatus = "active";
        try {
            emailService.sendAccountDetailsEmail(userDto.getEmail(), userDto.getMatriculeNumber(), generatedPassword);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending email");
        }

        Department department = departmentRepository.findById(userDto.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (userDto.getAccountType().toLowerCase().equals("employee")) {
            Employee newEmployee = new Employee(
                    userDto.getCIN(),
                    userDto.getMatriculeNumber(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    encodedPassword,
                    userDto.getAdress(),
                    userDto.getPhoneNumber(),
                    accountStatus
            );
            newEmployee.setDepartment(department);
            employeeRepository.save(newEmployee);

        } else if (userDto.getAccountType().toLowerCase().equals("manager")) {
            Manager newManager = new Manager(
                    userDto.getCIN(),
                    userDto.getMatriculeNumber(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    encodedPassword,
                    userDto.getAdress(),
                    userDto.getPhoneNumber(),
                    accountStatus
            );
            newManager.setDepartment(department);
            managerRepository.save(newManager);

        } else if (userDto.getAccountType().toLowerCase().equals("worker")) {
            Worker newWorker = new Worker(
                    userDto.getCIN(),
                    userDto.getMatriculeNumber(),
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    encodedPassword,
                    userDto.getAdress(),
                    userDto.getPhoneNumber(),
                    accountStatus
            );

            newWorker.setDepartment(department);
            workerRepository.save(newWorker);

        } else {
            return ResponseEntity.status(400).body("Invalid account type");
        }

        return ResponseEntity.ok("User created successfully");
    }

    // --- Generate Password Method ---
    private String generateRandomPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%&*-_";
        String allChars = upperCase + lowerCase + digits + specialChars;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }
    //


    // --- UpdateUserInfos ---
    public ResponseEntity<?> updateUser(int id, UpdateUserDto updatedUser) {
        Optional<User> user = userRepository.findById(id);
        try {
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setAdress(updatedUser.getAdress());
                existingUser.setCin(updatedUser.getCIN());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

                return ResponseEntity.status(200).body(userRepository.save(existingUser));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        throw new RuntimeException("User with the id " + updatedUser.getId() + " not Found!");
    }


    // --- ImageManagement ---
    public ResponseEntity<?> addImage(int id, MultipartFile image) {
        Optional<User> user = userRepository.findById(id);
        try {
            if (user.isPresent()) {
                User user_i = user.get();
                user_i.setImage(image.getBytes());
                return ResponseEntity.status(200).body(userRepository.save(user_i));
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error!");
        }
        throw new RuntimeException("User not existed!");
    }

    public ResponseEntity<?> updateImage(int id, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {
                    User existingUser = user.get();
                    existingUser.setImage(image.getBytes());
                    userRepository.save(existingUser);
                    return ResponseEntity.status(200).body("Image updated successfully");
                } else {
                    return ResponseEntity.status(404).body("User not found");
                }
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error updating image");
            }
        } else {
            return ResponseEntity.status(400).body("No image provided");
        }
    }


    public ResponseEntity<byte[]> getUserImage(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty() || userOptional.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOptional.get().getImage());
    }


    // --- ResetPassword ---
    public void sendPasswordResetCode(String email) {

        Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) {
            throw new RuntimeException("User not found.");
        }
        User user = u.get();

        if (!"active".equals(user.getStatus())) {
            throw new RuntimeException("Account is not activated yet.");
        }

        ResponseEntity<String> response = emailService.sendResetPasswordCode(email);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send password reset email.");
        }
    }

    public boolean verifyResetCode(String email, String code) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) {
            return false;
        }
        User user = u.get();
        return user.getCode().equals(code);
    }

    public ResponseEntity<String> resetPassword(ResetPasswordDto resetPasswordDto) {
        Optional<User> u = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }
        User user = u.get();
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setCode(null);
        try {
            userRepository.save(user);
            return ResponseEntity.ok("Password has been reset successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving new password: " + e.getMessage());
        }
    }

    public ResponseEntity<String> resetOldPassword(ResetPasswordDto resetPasswordDto) {
        Optional<User> optionalUser = userRepository.findById(resetPasswordDto.getId());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        User user = optionalUser.get();

        //Code syrine mel merge
    /*public ResponseEntity<String> resetOldPassword( ResetPasswordDto resetPasswordDto) {
        Optional<User> u = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }
        User user=u.get();*/

        if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }

        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));

        try {
            userRepository.save(user);

            ResponseEntity<String> emailResponse = emailService.sendUpdatingPasswordInfos(user.getEmail());
            if (!emailResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(500).body("Password updated, but failed to send email notification.");
            }

            return ResponseEntity.ok("Password has been reset successfully! Email notification sent.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving new password: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getUserbyId(int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isEmpty()) {
            return ResponseEntity.status(404).body("user not found");
        }
        User user = u.get();
        return ResponseEntity.ok(user);
    }

    // --- Reject Activation Request ---
    public ResponseEntity<String> rejectRequest(int id) {
        Optional<User> u = userRepository.findById(id);

        if (u.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }

        User user = u.get();
        emailService.sendRejectionEmail(user.getEmail());
        userRepository.delete(user);

        return ResponseEntity.ok("Account deleted and rejection email sent successfully");

    }


    public ResponseEntity<String> unblockAccount(int id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            User user = userOptional.get();
            if (!user.getStatus().equals("blocked")) {
                return ResponseEntity.badRequest().body("Account is not blocked");
            }

            user.setStatus("active");
            userRepository.save(user);
            emailService.sendAccountUnblockedEmailToUser(user.getEmail());

            // Reset login attempts
            loginAttemptService.resetAttempts(user.getEmail());

            return ResponseEntity.ok("Account unblocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error unblocking account");
        }
    }

    public ResponseEntity<List<User>> getAllAccounts( String status) {
        List<User> users;

        if ("active".equalsIgnoreCase(status)) {
            users = userRepository.findByStatus("active");
        } else if ("blocked".equalsIgnoreCase(status)) {
            users = userRepository.findByStatus("blocked");
        } else {
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.ok(users);
    }



}
