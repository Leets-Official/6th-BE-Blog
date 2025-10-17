package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID") // BIGINT 매핑
    private Long postId;
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계 (작성자)
    @JoinColumn(name = "USER_ID", nullable = false) // FK 컬럼
    private User user;

    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public Post() {}

    public Post(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    // Getter (Lombok 금지)
    public Long getPostId() { return postId; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

}
