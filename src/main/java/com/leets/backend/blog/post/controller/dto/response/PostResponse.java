package com.leets.backend.blog.post.controller.dto.response;

import com.leets.backend.blog.post.entity.Post;

import java.time.format.DateTimeFormatter;

public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String nickname;
    private String createdAt;
    private String updatedAt;

    private PostResponse(Long postId, String title, String content, String nickname, String createdAt, String updatedAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 정적 팩토리 메서드
    public static PostResponse from(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String created = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : null;
        String updated = post.getUpdatedAt() != null ? post.getUpdatedAt().format(formatter) : null;

        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getNickname(),
                created,
                updated
        );
    }

    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getNickname() { return nickname; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
