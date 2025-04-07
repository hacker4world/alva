package com.example.alvaBackend.Services;

import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.userRepository;
import com.example.alvaBackend.Security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class emailServices {

    private static final Logger logger = LoggerFactory.getLogger(emailServices.class);
    private final userRepository userRepository;
    private final JavaMailSender emailSender;
    private final JwtService jwtService;



    public emailServices(JavaMailSender emailSender,userRepository userRepository, JwtService jwtService){
        this.emailSender=emailSender;
        this.userRepository=userRepository;
        this.jwtService=jwtService;
    }

   public ResponseEntity<String> sendActivationEmail(String email) {
       Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        String activationLink = "http://localhost:8081/api/authentication/activateAccount?email=" + email;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(email);
        message.setSubject("Activation de votre compte");
        message.setText("Bonjour,\n\nVeuillez cliquer sur ce lien pour activer votre compte :\n\n"
                + activationLink + "\n\nCordialement,\nL'équipe admin.");

        emailSender.send(message);

        return ResponseEntity.ok("E-mail d'activation envoyé !");
    }

    // --- Rejection Email ---
    public ResponseEntity<String> sendRejectionEmail(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(email);
        message.setSubject("Account Activation Request Rejected");
        message.setText("Hello,\n\nWe regret to inform you that your account request has been rejected. " +
                "If you have any questions, please contact our support team.\n\nBest regards,\nThe Admin Team.");

        try {
            emailSender.send(message);
            logger.info("Rejection email sent to: " + email);
            return ResponseEntity.ok("Rejection email sent!");
        } catch (Exception e) {
            logger.error("Failed to send rejection email to: " + email, e);
            return ResponseEntity.status(500).body("Error sending rejection email!!");
        }
    }

    // --- SendAccountDetails ---
    public void sendAccountDetailsEmail(String email, String matricule, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(email);
        message.setSubject("Your Account Details");
        message.setText("Hello,\n\nYour account has been successfully created. Your login is: "
                + matricule + "\nYour temporary password is: " + password
                + "\n\nPlease log in and change your password as soon as possible.\n\nBest regards,\nThe Admin Team.");

        emailSender.send(message);
    }

    //Reset Password via Email
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public ResponseEntity<String> sendResetPasswordCode(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!!");
        }
        User user=u.get();
        String code = generateVerificationCode();
        user.setCode(code);


        try {
            userRepository.save(user);
            logger.info("Verification code saved for user: {}", email);
        } catch (Exception e) {
            logger.error("Error saving verification code for user: {}", email, e);
            return ResponseEntity.status(500).body("Failed to save verification code: " + e.getMessage());
        }


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(email);
        message.setSubject("Reset Password Code");
        message.setText("Your verification code is: " + code +"\n\n You can use it only one time !!");

        try {
            emailSender.send(message);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", email, e);
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }

        return ResponseEntity.ok("Reset Password Code sent !");
    }

    //Updating Password Infos
    public ResponseEntity<String> sendUpdatingPasswordInfos(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!!");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(email);
        message.setSubject("Password Updating");
        message.setText("Your password has been updated.");

        try {
            emailSender.send(message);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", email, e);
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }

        return ResponseEntity.ok(" Updating Password Infos sent !");
    }


    // --- Send Account Blocked Notification to Admin ---
    public ResponseEntity<String> sendAccountBlockedEmailToAdmin(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        String adminEmail = "marouanihana001@gmail.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(adminEmail);
        message.setSubject("User Account Permanently Blocked");
        message.setText("A user account has been permanently blocked due to too many failed login attempts.\n\n" +
                "User Details:\n" +
                "Matricule: " + user.getMatriculeNumber() + "\n" +
                "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                "Email: " + user.getEmail() + "\n\n" +
                "Please review this account.");

        try {
            emailSender.send(message);
            logger.info("Account blocked notification sent to admin for user: {}", userEmail);
            return ResponseEntity.ok("Account blocked notification sent to admin!");
        } catch (Exception e) {
            logger.error("Failed to send account blocked notification to admin for user: {}", userEmail, e);
            return ResponseEntity.status(500).body("Error sending account blocked notification to admin");
        }
    }

    // --- Send Account Blocked Notification to User ---
    public ResponseEntity<String> sendAccountBlockedEmailToUser(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Your Account Has Been Blocked");
        message.setText("Dear " + user.getFirstName() + ",\n\n" +
                "Your account has been permanently blocked due to too many failed login attempts.\n\n" +
                "For security reasons, we've disabled access to your account. " +
                "Please contact the administrator to resolve this issue.\n\n" +
                "Best regards,\nThe Support Team");

        try {
            emailSender.send(message);
            logger.info("Account blocked notification sent to user: {}", userEmail);
            return ResponseEntity.ok("Account blocked notification sent to user!");
        } catch (Exception e) {
            logger.error("Failed to send account blocked notification to user: {}", userEmail, e);
            return ResponseEntity.status(500).body("Error sending account blocked notification to user");
        }
    }

    public ResponseEntity<String> sendAccountUnblockedEmailToUser(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mhamedisirine9@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Your Account Has Been Unblocked");
        message.setText("Dear " + user.getFirstName() + ",\n\n" +
                "Your account has been successfully unblocked.\n\n" +
                "You can now log in and continue using your account as usual.\n\n" +
                "If you encounter any issues, contact our support team.\n\n" +
                "Best regards,\nThe Support Team");


        try {
            emailSender.send(message);
            logger.info("Account unblocked notification sent to user: {}", userEmail);
            return ResponseEntity.ok("Account unblocked notification sent to user!");
        } catch (Exception e) {
            logger.error("Failed to send account unblocked notification to user: {}", userEmail, e);
            return ResponseEntity.status(500).body("Error sending account unblocked notification to user");
        }
    }


}
