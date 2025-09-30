package com.leets.backend.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="comment")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="content_id", nullable=false)
    private Post post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User author;

    @Lob @Column(name="content", nullable = false)
    private String content;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public Comment(){}

    public Long getId(){return id;}
    public Post getPost(){return post;}
    public User getAuthor(){return author;}
    public String getContent(){return content;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}
