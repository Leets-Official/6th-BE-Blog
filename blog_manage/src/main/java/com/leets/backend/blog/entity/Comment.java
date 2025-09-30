package com.leets.backend.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Getters
    public Long getCommentId() {
        return commentId;
    }
    public String getContent() {
        return content;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public User getUser() {
        return user;
    }
    public Post getPost() {
        return post;
    }
}
