package com.leets.backend.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leets.backend.blog.entity.Post;
import java.time.LocalDateTime;

public class PostResponseDTO {
    private Long post_id;
    private String title;
    private String content;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public PostResponseDTO() {}

    // Post 엔티티 -> DTO 변환 메서드
    public static PostResponseDTO from(Post post) {
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.post_id = post.getPostId();
        responseDTO.title = post.getTitle();
        responseDTO.content = post.getContent();
        responseDTO.nickname = post.getUser().getNickname();
        responseDTO.createdAt = post.getCreatedAt();
        responseDTO.updatedAt = post.getUpdatedAt();

        return responseDTO;
    }

    // getters
    public Long getPost_id() {
        return post_id;
    }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public String getNickname() { return nickname; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
