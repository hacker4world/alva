package com.example.alvaBackend.Dto;

public class CommentReplyResponseDto {
    private int id;
    private int commentId;
    private String content;

    public CommentReplyResponseDto(int id, int commentId, String content) {
        this.id = id;
        this.commentId = commentId;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
