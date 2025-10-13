package com.leets.backend.blog.entity;

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

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 게시물 삭제 여부
    private Boolean deleted = false;

    protected Post() {
        // JPA 기본 생성자
    }

    // 생성자에서 값 할당
    public Post(User user, String title, String content){
        this.user =  user;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.deleted = false; // 생성 시점에는 삭제되지 않은 상태이므로
    }

    // Getters
    public Long getPostId() { return postId; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
