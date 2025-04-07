package com.example.alvaBackend.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;


@Component
public class JwtService {
    private final String key = "l7qnA0XWzp4okYJIMRso3vusMGzL6i0XuQiVStXl3kJqc/4/oOP2+EmXO1QOwVBmKoqcpKQQn/zY/qBRqLo79sPN+m6bJIU8h00y3B2iJqua7fbfWDERmSKnmQ8kBzAFqtUwWyesPLHIOzX9RX5gYdiFahR9aW8TC+mlMi7dwDaNgN+prO0YPfdVz6omTtbUNQ/SM7QXwHD+IuPyrBpvhbG5xPdH0SCiN08F3PBy8uI5xrRR6cyWRktAkFaZDzpi96VZXB/MLNMXcty5NH/py0i9IdMd31HWx1tzuIRsdJRqkikfnoJpkN4A0EK3d393zypQUmwtqhfzGrgHCtZkAYCUjUqvT5lFSgpdJHeKYKs=\n";
    public String createToken(String email) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String email) {
        return email.equals(extractEmail(token));
    }

    //Activation Token
    public String createActivationToken(String email,long expirationDuration) {
            return Jwts.builder()
                    .setClaims(new HashMap<>())
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+expirationDuration))
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();
        }

    public String extractEmailForValidation(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateActivationToken(String token, String email) {
        try {
            String extractedEmail = extractEmailForValidation(token);
            return email.equals(extractedEmail);
        } catch (JwtException e) {
            return false;
        }
    }



}