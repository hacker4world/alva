package com.example.alvaBackend.Dto;

import jakarta.persistence.Lob;

import java.time.LocalDateTime;

public class UpdatePostDto {
    private int pub_id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime publication_date;
    private LocalDateTime last_modification_date;
    private String category;
    @Lob
    private byte[] attachment;

    public UpdatePostDto() {
    }

    public UpdatePostDto(int pub_id, String title, String description,
                         String content, LocalDateTime last_modification_date,
                         String category, byte[] attachment) {
        this.pub_id = pub_id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.last_modification_date = last_modification_date;
        this.category = category;
        this.attachment = attachment;
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

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
