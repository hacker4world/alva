package com.example.alvaBackend.Controllers;

import com.example.alvaBackend.Dto.DeleteAccountDto;
import com.example.alvaBackend.Dto.ResponseApi;
import com.example.alvaBackend.Dto.UserDto;
import com.example.alvaBackend.Dto.loginDto;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.userRepository;
import com.example.alvaBackend.Security.JwtService;
import com.example.alvaBackend.Services.EmailService;
import com.example.alvaBackend.Services.authenticationService;
import com.example.alvaBackend.Services.emailServices;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authentication")
public class authenticationController {

    private final authenticationService authService;
    private final emailServices emailServices;
    private final EmailService emaillService;
    private final userRepository userRepository;
    private final JwtService jwtService;

    public authenticationController(authenticationService authService,emailServices emailServices,
                                    userRepository userRepository,EmailService emaillService,
                                    JwtService jwtService){
        this.authService=authService;
        this.emaillService=emaillService;
        this.emailServices=emailServices;
        this.userRepository=userRepository;
        this.jwtService=jwtService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        return authService.createUser(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseApi>login(@RequestBody loginDto l){
        return authService.login(l);
    }

    @PostMapping("/sendActivationEmail")
    public ResponseEntity<String> sendActivationEmail(@RequestParam("email") String email) {
        return emailServices.sendActivationEmail(email);
    }

    @GetMapping("/activateAccount")
    public ResponseEntity<String> activateAccount(@RequestParam("email") String email) {
        return authService.activateAccount(email);

    }

    @DeleteMapping(value = "/deleteAccount")
    public ResponseEntity<String> deleteAccount(@Valid @RequestBody DeleteAccountDto accountDto) {
        return authService.deleteAccount(accountDto);
    }

    @GetMapping("/inactiveAccount")
    public ResponseEntity<List<User>> getInactiveAccount(@RequestParam("status") String status) {
        return authService.getInactiveAccount(status);
    }

    @GetMapping("/getByMatricule/{matricule}")
    public ResponseEntity<User> getByMatricule(@PathVariable String matricule) {
        Optional<User> u = userRepository.findByMatriculeNumber(matricule);
        User user = null;
        if (u.isPresent()) {
            user = u.get();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/approve/{token}")
    public ResponseEntity<String> approveUser(@PathVariable String token) {
        try {
            String email = jwtService.extractEmail(token);
            if (email == null || !jwtService.validateActivationToken(token, email)) {
                return ResponseEntity.status(400).body("Invalid or expired token");
            }

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(400).body("User not found");
            }

            User user = userOptional.get();
            userRepository.save(user);
            emaillService.sendUserActivationEmail(user.getEmail(), token);
            return ResponseEntity.ok("User approved. Activation email sent.");
        } catch (JwtException e) {
            return ResponseEntity.status(400).body("Token validation failed: " + e.getMessage());
        }
    }

    @GetMapping("/user/activate")
    public ResponseEntity<String> activateUser(@PathVariable String token) {
        try {
            String email = jwtService.extractEmail(token);
            if (email == null || !jwtService.validateActivationToken(token, email)) {
                return ResponseEntity.status(400).body("Invalid or expired token");
            }

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(400).body("User not found");
            }

            User user = userOptional.get();
            user.setStatus("pending");
            user.setCode(null); // Token is used, remove it
            userRepository.save(user);
          /*  try {
                response.sendRedirect("http://localhost:5730/login");
            } catch (IOException e) {
                return ResponseEntity.status(500).build();
            }*/
            //-----front-------
            /*fetch('http://localhost:8081/api/authentication/user/activate?token=' + token)
      .then(response => {
        if (response.ok) {
          window.location.href = "/login"; // Redirection après activation
        }
      });
    */
            //---ou bien----
            return ResponseEntity.ok("<h2>Account activated successfully!</h2> <a href='http://localhost:5173/login'>Go to Login</a>");
        } catch (JwtException e) {
            return ResponseEntity.status(400).body("Token validation failed: " + e.getMessage());
        }

    }
}



