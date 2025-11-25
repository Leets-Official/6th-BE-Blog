package com.leets.backend.blog_manage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 댓글 엔티티
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment() {}

    /** 댓글 생성 생성자 */
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    /** 댓글 수정 */
    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }


    // --- Getters ---
    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public User getUser() { return user; }
    public Post getPost() { return post; }
}