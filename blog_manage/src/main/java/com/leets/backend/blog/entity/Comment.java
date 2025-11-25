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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Comment() {}

    public static Comment createComment(String content, User user, Post post) {
        Comment comment = new Comment();
        comment.content = content;
        comment.user = user;
        comment.post = post;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    public void updateComment(String newContent) {
        this.content = newContent;
    }

    // Getters
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
