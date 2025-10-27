package com.leets.backend.blog.dto;

import java.time.LocalDateTime;

public class PostResponseDTO {

    private Long postId;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public PostResponseDTO() {}

    public PostResponseDTO(Long postId, String title, String content, String nickname, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getNickname() { return nickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
