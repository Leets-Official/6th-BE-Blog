package com.leets.backend.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "post")
    public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long postId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(length = 255)
        private String title;

        @Lob
        private String content;

        @Column(length = 1024)
        private String imageUrl;

        private LocalDateTime createdAt = LocalDateTime.now();

    public Post() {}

    public Post(Long id, String title, String content, LocalDateTime createdAt) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getpostId() {
        return postId;
    }

    public void setpostId(Long id) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
