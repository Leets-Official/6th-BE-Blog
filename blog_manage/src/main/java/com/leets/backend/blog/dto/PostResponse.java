package com.leets.backend.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leets.backend.blog.entity.Post;
import java.time.LocalDateTime;

public class PostResponse {

    private Long postId;
    private String title;
    private String content;
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public PostResponse() {}


    public static PostResponse from(Post post) {
        PostResponse response = new PostResponse();
        response.postId = post.getPostId();
        response.title = post.getTitle();
        response.content = post.getContent();
        response.nickname = post.getUser().getNickname();
        response.createdAt = post.getCreatedAt();
        response.updatedAt = post.getUpdatedAt();
        return response;
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public String getNickname() { return nickname; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
