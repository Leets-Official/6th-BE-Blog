package com.leets.backend.blog_manage.dto;

import com.leets.backend.blog_manage.entity.Post;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PostListResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String authorNickname;
    private final String createdAt;
    private final int commentCount;
    private final String thumbnailUrl;

    public PostListResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent().length() > 100 ? post.getContent().substring(0, 100) : post.getContent();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = formatCreatedAt(post.getCreatedAt());
        this.commentCount = post.getComments() != null ? post.getComments().size() : 0;
        this.thumbnailUrl = post.getThumbnailUrl();
    }

    private String formatCreatedAt(LocalDateTime createdAt) {
        long hours = Duration.between(createdAt, LocalDateTime.now()).toHours();
        if (hours < 24) {
            return hours + "시간 전";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorNickname() { return authorNickname; }
    public String getCreatedAt() { return createdAt; }
    public int getCommentCount() { return commentCount; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}