package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponseDTO {

    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public CommentResponseDTO() { }

    public static CommentResponseDTO from(Comment comment) {
        CommentResponseDTO responseDTO = new CommentResponseDTO();

        responseDTO.commentId = comment.getCommentId();
        responseDTO.content = comment.getContent();
        responseDTO.nickname = comment.getUser().getNickname();
        responseDTO.createdAt = comment.getCreatedAt();

        return responseDTO;
    }

    // getters
    public Long getCommentId() { return commentId; }
    public String getContent() { return content; }
    public String getNickname() { return nickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
