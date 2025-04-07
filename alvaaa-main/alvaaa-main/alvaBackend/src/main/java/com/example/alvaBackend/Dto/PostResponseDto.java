package com.example.alvaBackend.Dto;


import jakarta.persistence.Lob;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {
    private int pub_id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime publication_date;
    private LocalDateTime last_modification_date;
    private String category;
    private String status;
    private byte[] attachment;
    private int likeCount;

    private List<CommentResponseDto> comments;

    public PostResponseDto(int pub_id, String title, String description, String content, LocalDateTime publication_date, LocalDateTime last_modification_date, String category, String status, byte[] attachment, int likeCount, List<CommentResponseDto> comments) {
        this.pub_id = pub_id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.publication_date = publication_date;
        this.last_modification_date = last_modification_date;
        this.category = category;
        this.status = status;
        this.attachment = attachment;
        this.likeCount = likeCount;
        this.comments = comments;
    }

    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(LocalDateTime publication_date) {
        this.publication_date = publication_date;
    }

    public LocalDateTime getLast_modification_date() {
        return last_modification_date;
    }

    public void setLast_modification_date(LocalDateTime last_modification_date) {
        this.last_modification_date = last_modification_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }
}
