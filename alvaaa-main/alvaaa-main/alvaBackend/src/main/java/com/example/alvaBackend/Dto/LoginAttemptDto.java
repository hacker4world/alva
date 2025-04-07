package com.example.alvaBackend.Dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class LoginAttemptDto {
    private int failedAttempts;
    private LocalDateTime lockTime;
    private boolean isPermanentlyBlocked;

    public LoginAttemptDto() {
        this.failedAttempts = 0;
        this.lockTime = null;
        this.isPermanentlyBlocked = false;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void increaseFailedAttempts() {
        this.failedAttempts++;

        if (this.failedAttempts >= 6) {
            this.isPermanentlyBlocked = true;
        } else if (this.failedAttempts >= 3) {
            this.lockTime = LocalDateTime.now();
        }
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
        this.lockTime = null;
        this.isPermanentlyBlocked = false;
    }

    public boolean isLocked() {
        if (this.isPermanentlyBlocked) {
            return true;
        }

        if (this.lockTime == null) {
            return false;
        }

        long secondsPassed = Duration.between(this.lockTime, LocalDateTime.now()).toSeconds();
        int lockDuration = getLockDuration();

        return secondsPassed < lockDuration;
    }

    public long getRemainingLockTime() {
        if (this.lockTime == null) {
            return 0;
        }

        int lockDuration = getLockDuration();
        long secondsPassed = Duration.between(this.lockTime, LocalDateTime.now()).toSeconds();
        long remainingTime = lockDuration - secondsPassed;

        return Math.max(remainingTime, 0);
    }

    private int getLockDuration() {
        switch (failedAttempts) {
            case 3: return 30;  // 30 seconds
            case 4: return 60;  // 60 seconds
            case 5: return 90;  // 90 seconds
            default: return 0;  // No lock for less than 3 attempts
        }
    }

    public boolean isPermanentlyBlocked() {
        return isPermanentlyBlocked;
    }
}
