package com.example.alvaBackend.Dto;

import com.example.alvaBackend.Entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class PostDto {
    private int pub_id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime publication_date;
    private String category;
    private String status;
    @Lob
    private byte[] attachment;
    private int user_id;

    public PostDto() {
    }

    /*public PostDto(String title, String description, String content,
                   LocalDateTime publication_date, String category,
                   String status, String attachment_path, int user_id) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publication_date = publication_date;
        this.category = category;
        this.status = status;
        this.attachment_path = attachment_path;
        this.user_id = user_id;
    }*/

    public PostDto(String title, String description, String content,
                   LocalDateTime publication_date, String category,
                   String status , byte[] attachment, int user_id) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publication_date = publication_date;
        this.category = category;
        this.status = status;
        this.attachment = attachment;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
