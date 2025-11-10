package com.leets.backend.blog.comment.entity;

import com.leets.backend.blog.post.entity.Post;
import com.leets.backend.blog.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected Comment() {
    }

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드
    public static Comment of(Post post, User user, String content) {
        return new Comment(post, user, content);
    }

    // getter
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
}

