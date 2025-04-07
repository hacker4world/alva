package com.example.alvaBackend.Dto;

import java.time.LocalDateTime;

public class CommentResponseDto {

    private int id;  // Changé de Long à int

    private String content;
    private LocalDateTime createdAt;

    private String userName;

    public CommentResponseDto(int id, String content, LocalDateTime createdAt, String userName) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
