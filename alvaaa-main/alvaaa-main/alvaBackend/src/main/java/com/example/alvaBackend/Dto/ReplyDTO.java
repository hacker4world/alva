package com.example.alvaBackend.Dto;

public class ReplyDTO {
    private int commentId;
    private int userId;
    private String content;

    public int getParentCommentId() {
        return commentId;
    }

    public void setParentCommentId(int parentCommentId) {
        this.commentId = parentCommentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}