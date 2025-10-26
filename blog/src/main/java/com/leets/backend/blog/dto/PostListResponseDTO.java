package com.leets.backend.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PostListResponseDTO {
    private Long postId;
    private String title;
    private String nickname;
    private Integer commentCount; // 댓글 수
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public PostListResponseDTO(Long postId, String title, String nickname, Integer commentCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.title = title;
        this.nickname = nickname;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters
    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getNickname() { return nickname; }
    public Integer getCommentCount() { return commentCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
