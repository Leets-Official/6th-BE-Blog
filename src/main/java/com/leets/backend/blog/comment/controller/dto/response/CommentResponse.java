package com.leets.backend.blog.comment.controller.dto.response;

import com.leets.backend.blog.comment.entity.Comment;
import java.time.format.DateTimeFormatter;

public class CommentResponse {
    // 객체 생성 시 필드 주입을 위한 생성자
    private Long id;
    private Long postId;
    private Long userId;
    private String nickname;
    private String content;
    private String createdAt;
    private String updatedAt;

    public CommentResponse(Long id, Long postId, Long userId, String nickname, String content, String createdAt, String updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 정적 팩토리 메서드
    public static CommentResponse from(Comment comment) {
        String createdAt = comment.getCreatedAt() != null
                ? comment.getCreatedAt().format(FORMATTER)
                : null;

        String updatedAt = comment.getUpdatedAt() != null
                ? comment.getUpdatedAt().format(FORMATTER)
                : null;

        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                createdAt,
                updatedAt
        );
    }

    // getter
    public Long getId() {
        return id;
    }
    public Long getPostId() {
        return postId;
    }
    public Long getUserId() {
        return userId;
    }
    public String getNickname() {
        return nickname;
    }
    public String getContent() {
        return content;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
}
