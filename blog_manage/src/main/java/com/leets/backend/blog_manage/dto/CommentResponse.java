package com.leets.backend.blog_manage.dto;

import com.leets.backend.blog_manage.entity.Comment;
import java.time.LocalDateTime;

public class CommentResponse {
    private final Long id;
    private final String content;
    private final String authorNickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getAuthorNickname() { return authorNickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}