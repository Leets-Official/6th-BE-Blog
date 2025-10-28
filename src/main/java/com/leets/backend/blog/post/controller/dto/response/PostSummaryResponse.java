package com.leets.backend.blog.post.controller.dto.response;

import com.leets.backend.blog.post.entity.Post;

public class PostSummaryResponse {
    private Long postId;
    private String title;
    private String content;

    private PostSummaryResponse(Long postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }

    public static PostSummaryResponse from(Post post) {
        return new PostSummaryResponse(post.getPostId(), post.getTitle(),  post.getContent());
    }

    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}
