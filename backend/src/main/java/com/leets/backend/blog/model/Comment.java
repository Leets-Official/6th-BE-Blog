package com.leets.backend.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();


    // 기본 생성자
    public Comment() {}

    // 전체 생성자
    public Comment(Long commentId, Post post, User user, String content,
                    LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.commentId = commentId;
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }


}

