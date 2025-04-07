package com.example.alvaBackend.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pub_id;
    private String title;
    private String description;
    @Lob
    private String content;
    private LocalDateTime publication_date;
    private LocalDateTime last_modification_date;
    private String category;
    private String status;
    @Lob
    private byte[] attachment;
    private int likeCount = 0;

    //Image and Video Storage ( to change later for performance causes)
    // : think later to store big size videos and image collections
    //private String attachment_path;
    //

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public Post(String title, String description, String content,
                LocalDateTime publication_date, String category,
                String status, byte[] attachment, User user) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publication_date = publication_date;
        this.category = category;
        this.status = status;
        this.attachment = attachment;
        this.user = user;
    }

    public Post(String title, String description, String content, LocalDateTime publication_date,
                LocalDateTime last_modification_date, String category, String status,
                byte[] attachment, User user) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publication_date = publication_date;
        this.last_modification_date = last_modification_date;
        this.category = category;
        this.status = status;
        this.attachment = attachment;
        this.user = user;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}