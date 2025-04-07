package com.example.alvaBackend.Entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reaction_id;
    private int likeCount;
    private String comment;
    private LocalDateTime comment_date;
    private LocalDateTime comment_modification_date;
    private LocalDateTime like_date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "pub_id", nullable = false)
    private Post post;

    public Reaction() {
    }

    public Reaction(int reaction_id, int likeCount, String comment) {
        this.reaction_id = reaction_id;
        this.likeCount = likeCount;
        this.comment = comment;
    }

    public Reaction(int likeCount, String comment, LocalDateTime comment_date,
                    LocalDateTime comment_modification_date, LocalDateTime like_date,
                    User user, Post post) {
        this.likeCount = likeCount;
        this.comment = comment;
        this.comment_date = comment_date;
        this.comment_modification_date = comment_modification_date;
        this.like_date = like_date;
        this.user = user;
        this.post = post;
    }

    public int getReaction_id() {
        return reaction_id;
    }

    public void setReaction_id(int reaction_id) {
        this.reaction_id = reaction_id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getComment_date() {
        return comment_date;
    }

    public void setComment_date(LocalDateTime comment_date) {
        this.comment_date = comment_date;
    }

    public LocalDateTime getComment_modification_date() {
        return comment_modification_date;
    }

    public void setComment_modification_date(LocalDateTime comment_modification_date) {
        this.comment_modification_date = comment_modification_date;
    }

    public LocalDateTime getLike_date() {
        return like_date;
    }

    public void setLike_date(LocalDateTime like_date) {
        this.like_date = like_date;
    }
}