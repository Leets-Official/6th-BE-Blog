package com.leets.backend.blog.dto;

import com.leets.backend.blog.domain.Comment;
import java.time.LocalDateTime;

public class CommentResponse {

    private final Long commentId;
    private final String content;
    private final String authorNickname;
    private final boolean isOwner;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment, boolean isOwner) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = comment.getUser().getNickname();
        this.isOwner = isOwner;
        this.createdAt = comment.getCreatedAt();
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}