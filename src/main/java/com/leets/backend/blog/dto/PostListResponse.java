package com.leets.backend.blog.dto;

import com.leets.backend.blog.domain.Post;
import java.time.LocalDateTime;

public class PostListResponse {

    private final Long postId;
    private final String title;
    private final String authorNickname;
    private final int commentCount;
    private final LocalDateTime createdAt;

    public PostListResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getUser().getNickname();
        this.commentCount = post.getComments().size();
        this.createdAt = post.getCreatedAt();
    }

    // --- Getters ---
    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}