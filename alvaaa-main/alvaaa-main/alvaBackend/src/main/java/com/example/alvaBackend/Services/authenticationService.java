package com.example.alvaBackend.Services;


import com.example.alvaBackend.Dto.*;
import com.example.alvaBackend.Entities.*;
import com.example.alvaBackend.Repositories.*;
import com.example.alvaBackend.Security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class authenticationService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final employeeRepository employeeRepository;
    private final managerRepository managerRepository;
    private final workerRepository workerRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final emailServices emailService;
    private final EmailService emaillService;
    private final loginAttemptService loginAttemptService;
    private final DepartmentRepository departmentRepository;
    private  final PostRepository postRepository;
    private final ReactionRepository reactionRepository;


    public authenticationService(com.example.alvaBackend.Repositories.userRepository userRepository, PasswordEncoder passwordEncoder, com.example.alvaBackend.Repositories.employeeRepository employeeRepository, com.example.alvaBackend.Repositories.managerRepository managerRepository, com.example.alvaBackend.Repositories.workerRepository workerRepository, AuthenticationProvider authenticationProvider, JwtService jwtService, emailServices emailService, EmailService emaillService, com.example.alvaBackend.Services.loginAttemptService loginAttemptService, DepartmentRepository departmentRepository, PostRepository postRepository, ReactionRepository reactionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.workerRepository = workerRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.emaillService = emaillService;
        this.loginAttemptService = loginAttemptService;
        this.departmentRepository = departmentRepository;
        this.postRepository = postRepository;
        this.reactionRepository = reactionRepository;
    }

    public ResponseEntity<String> createUser(UserDto userDto) {

        Optional<User> u = userRepository.findByEmailOrMatriculeNumberOrCin(
                userDto.getEmail(), userDto.getMatriculeNumber(), userDto.getCIN());

        if (u.isPresent()) {
            return ResponseEntity.status(400).body("User already exists with this email, matricule, or CIN");
        }

        if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(400).body("Password cannot be null or empty");
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        // String token = emaillService.generateToken();
        // long expirationTimestamp = emaillService.generateExpirationTimestamp();
        String token=jwtService.createActivationToken(userDto.getEmail(),3*60*1000);

        Department department = departmentRepository.findById(userDto.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User newUser;

        if (userDto.getAccountType().equalsIgnoreCase("employee")) {
            newUser = new Employee(userDto.getCIN(), userDto.getMatriculeNumber(), userDto.getFirstName(),
                    userDto.getLastName(), userDto.getEmail(), encodedPassword, userDto.getAdress(),
                    userDto.getPhoneNumber(), "inactive");
        } else if (userDto.getAccountType().equalsIgnoreCase("manager")) {
            newUser = new Manager(userDto.getCIN(), userDto.getMatriculeNumber(), userDto.getFirstName(),
                    userDto.getLastName(), userDto.getEmail(), encodedPassword, userDto.getAdress(),
                    userDto.getPhoneNumber(), "inactive");
        } else if (userDto.getAccountType().equalsIgnoreCase("worker")) {
            newUser = new Worker(userDto.getCIN(), userDto.getMatriculeNumber(), userDto.getFirstName(),
                    userDto.getLastName(), userDto.getEmail(), encodedPassword, userDto.getAdress(),
                    userDto.getPhoneNumber(), "inactive");
        } else {
            return ResponseEntity.status(400).body("Invalid account type");
        }

        newUser.setDepartment(department);
        newUser.setCode(token);
        userRepository.save(newUser);


        //emaillService.sendAdminApprovalEmail("sofien@gmail.com", userDto.getEmail(), token);
        emaillService.sendAdminApprovalEmail("sss@gmail.com", userDto.getEmail(), token);

        return ResponseEntity.ok("User created successfully. Awaiting admin approval.");
    }


    public ResponseEntity<ResponseApi> login(loginDto l) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(l.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus().equals("blocked")) {
                    return ResponseEntity.status(403).body(new ResponseApi(
                            "Your account has been permanently blocked. Please contact support."
                    ));
                }
            }

            LoginAttemptDto attempt = loginAttemptService.getUserAttempts(l.getEmail());
            System.out.println("Login attempts: " + attempt.getFailedAttempts()); // Debugging

            if (attempt.isPermanentlyBlocked()) {
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (!user.getStatus().equals("blocked")) {
                        user.setStatus("blocked");
                        userRepository.save(user);
                        emailService.sendAccountBlockedEmailToAdmin(l.getEmail());
                        emailService.sendAccountBlockedEmailToUser(l.getEmail());
                    }
                }
                return ResponseEntity.status(403).body(new ResponseApi(
                        "Your account has been permanently blocked due to too many failed attempts. Please contact support.",
                        attempt.getFailedAttempts()
                ));
            }

            if (attempt.isLocked()) {
                long remainingTime = attempt.getRemainingLockTime();
                return ResponseEntity.status(403).body(new ResponseApi(
                        "Account temporarily locked. Try again in " + remainingTime + " seconds.",
                        attempt.getFailedAttempts(), // Include failed attempts
                        remainingTime // Include remaining lock time
                ));
            }

            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(l.getEmail(), l.getPassword())
            );

            if (authentication.isAuthenticated()) {
                Optional<User> user = userRepository.findByEmail(l.getEmail());
                String role = "";
                User u = user.get();

                if (u instanceof Admin) {
                    role = "admin";
                } else if (u instanceof Employee) {
                    role = "employee";
                } else if (u instanceof Worker) {
                    role = "worker";
                } else {
                    role = "manager";
                }

                if (u.getStatus().equals("pending")) {
                    u.setStatus("active");
                    userRepository.save(u);
                }
                loginAttemptService.resetAttempts(l.getEmail());

                return ResponseEntity.ok(new ResponseApi(jwtService.createToken(l.getEmail()), u.getId(), role, "Logged in", u.getStatus()));
            }

            // If authentication fails, increment attempts
            attempt.increaseFailedAttempts();
            return ResponseEntity.status(401).body(new ResponseApi(
                    "Invalid credentials. Failed attempts: " + attempt.getFailedAttempts(),
                    attempt.getFailedAttempts() // Include failed attempts count
            ));

        } catch (AuthenticationException e) {
            LoginAttemptDto attempt = loginAttemptService.getUserAttempts(l.getEmail());
            attempt.increaseFailedAttempts();

            return ResponseEntity.status(401).body(new ResponseApi(
                    "Invalid email or password. Failed attempts: " + attempt.getFailedAttempts(),
                    attempt.getFailedAttempts() // Include failed attempts count
            ));
        }
    }


    public ResponseEntity<String> activateAccount(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("user not found");
        }
        User u=user.get();
        if ("active".equals(u.getStatus())) {
            return ResponseEntity.ok("Your account is already active!");
        }

        u.setStatus("active");
        userRepository.save(u);

        return ResponseEntity.ok("Your account has been successfully activated!");
    }
    @Transactional
    public ResponseEntity<String> deleteAccount(DeleteAccountDto accountData) {
        Optional<User> u = userRepository.findByEmail(accountData.getEmail());

        if (u.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
List<Post> posts=postRepository.findByEmailUser(accountData.getEmail());
        if(!posts.isEmpty()){
            for (Post post : posts){
                post.setUser(null);
                postRepository.save(post);
            }
        }
        User user = u.get();

        if (!accountData.getMatriculeNumber().equals(user.getMatriculeNumber())) {
            return ResponseEntity.status(403).body("Incorrect matricule number");
        }


    /*    if (user.getReactions() != null) {
            for (Reaction reaction : user.getReactions()) {
                reactionRepository.delete(reaction);
            }
        }*/


     /*   if (user.getPosts() != null) {
            for (Post post : user.getPosts()) {
                post.setUser(null);
                postRepository.save(post);
            }
        }*/


        userRepository.delete(user);

        return ResponseEntity.ok("Account deleted successfully");
    }


    public ResponseEntity<List<User>> getInactiveAccount(String status) {
        List<User> userList = userRepository.findByStatus(status);

        if ("active".equals(status)) {
            List<User> filteredList = new ArrayList<>();

            for (User user : userList) {
                if (!(user instanceof Admin)) {
                    filteredList.add(user);
                }
            }

            userList = filteredList;
        }

        return ResponseEntity.ok(userList);
    }

}
