package com.leets.backend.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    //Getters
    public Long getCommentId() {
        return commentId;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public User getUser() {
        return user;
    }
    public Post getPost() {
        return post;
    }
}
