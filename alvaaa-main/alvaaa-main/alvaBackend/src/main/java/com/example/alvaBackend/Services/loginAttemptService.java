package com.example.alvaBackend.Services;

import com.example.alvaBackend.Dto.LoginAttemptDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class loginAttemptService {

        private final Map<String, LoginAttemptDto> attemptsCache = new ConcurrentHashMap<>();

        public LoginAttemptDto getUserAttempts(String email) {
            return attemptsCache.computeIfAbsent(email, k -> new LoginAttemptDto());
        }

        public void resetAttempts(String email) {
            attemptsCache.remove(email);
        }
    }

