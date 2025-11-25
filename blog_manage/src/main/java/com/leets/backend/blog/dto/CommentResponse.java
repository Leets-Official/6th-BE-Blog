package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public CommentResponse() {}
    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();

        response.commentId = comment.getCommentId();
        response.content = comment.getContent();
        response.nickname = comment.getUser().getNickname();
        response.createdAt = comment.getCreatedAt();

        return response;
    }
    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
