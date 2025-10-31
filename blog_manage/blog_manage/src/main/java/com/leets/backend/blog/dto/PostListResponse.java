package com.leets.backend.blog.dto;

import com.leets.backend.blog.entity.Post;

import java.time.LocalDateTime;

public class PostListResponse {
    private final Long id;
    private final String title;
    private final String authorNickname;
    private final String contentSnippet;
    private final LocalDateTime createdAt;

    public PostListResponse(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();

        if (post.getContent() != null && post.getContent().length() > 15) {
            this.contentSnippet = post.getContent().substring(0, 15) + "...";
        } else {
            this.contentSnippet = post.getContent();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public String getContentSnippet() {
        return contentSnippet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
