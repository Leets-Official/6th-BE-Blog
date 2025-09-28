package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column(nullable = false)
    private LocalDate updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Getters
    public Long getPostId() {
        return postId;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    public User getUser() {
        return user;
    }
}
