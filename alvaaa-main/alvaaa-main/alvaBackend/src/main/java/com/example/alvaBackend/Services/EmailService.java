package com.example.alvaBackend.Services;

import com.example.alvaBackend.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtService jwtService;

    /*public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public long generateExpirationTimestamp() {
        return LocalDateTime.now().plusHours(48).toEpochSecond(ZoneOffset.UTC);
    }
*/


   public void sendAdminApprovalEmail(String adminEmail, String userEmail,String token) {
        String activationLink = "http://localhost:8081/api/authentication/admin/approve/" + token;
        String subject = "New User Registration Approval";
        String message = "A new user (" + userEmail + ") has registered. Click the link to approve: " + activationLink;

        sendEmail(adminEmail, subject, message);
    }

    /*public void sendAdminApprovalEmail(String adminEmail, String userEmail, String token) {
        String activationLink = "http://localhost:8081/api/authentication/admin/approve/" + token;
        String subject = "Approval Required: New User Registration";

        // Chargez le fichier HTML template
        String messageTemplate = "";
        try {
            messageTemplate = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/email-template.html")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        messageTemplate = messageTemplate.replace("{{userEmail}}", userEmail);
        messageTemplate = messageTemplate.replace("{{activationLink}}", activationLink);

        sendEmail(adminEmail, subject, messageTemplate);
    }*/

    public void sendUserActivationEmail(String userEmail, String token) {
        String activationLink = "http://localhost:8081/api/authentication/user/activate/" + token;
        String subject = "Your Account is Approved";
        String message = "Your account has been approved. Click the link to activate your account: " + activationLink;

        sendEmail(userEmail, subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
