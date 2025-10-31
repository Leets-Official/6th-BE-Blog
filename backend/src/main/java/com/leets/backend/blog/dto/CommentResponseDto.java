package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        String content,
        String nickname,
        LocalDateTime createdAt
) {
    public static CommentResponseDto from(Comment c) {
        return new CommentResponseDto(
                c.getCommentId(),
                c.getContent(),
                c.getUser().getNickname(),
                c.getCreatedAt()
        );
    }
}
