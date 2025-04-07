package com.example.alvaBackend.Dto;

public class ResponseApi {
    public String Token;
    public int id;
    public String role;
    public String message;
    public String status;
    public int failedAttempts;
    public long remainingTime;

     public ResponseApi(){

     }
    public ResponseApi(String token, int id, String role, String message, String status) {
        Token = token;
        this.id = id;
        this.role = role;
        this.message = message;
        this.status = status;
    }

    public ResponseApi(String token, String role, String message, int id) {
        Token = token;
        this.role = role;
        this.message = message;
        this.id = id;
    }

    public ResponseApi(String message) {
        this.message = message;
    }


    public ResponseApi(String message,long remainingTime) {
         this.message=message;
        this.remainingTime = remainingTime;
    }

    public ResponseApi(String message, int failedAttempts, long remainingTime) {
        this.message = message;
        this.failedAttempts = failedAttempts;
        this.remainingTime = remainingTime;
    }
}
