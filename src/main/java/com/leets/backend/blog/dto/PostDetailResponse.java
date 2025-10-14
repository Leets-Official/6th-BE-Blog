package com.leets.backend.blog.dto;

import com.leets.backend.blog.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDetailResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String authorNickname;
    private final LocalDateTime createdAt;
    private final boolean isOwner;
    private final List<CommentResponse> comments;

    // 생성자: Post 모델과 소유권 여부를 받아 DTO 필드를 초기화합니다.
    public PostDetailResponse(Post post, boolean isOwner) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.isOwner = isOwner;
        this.comments = post.getComments().stream()
                .map(comment -> new CommentResponse(comment, true)) // isOwner 로직은 실제 구현에 맞게 수정 필요
                .collect(Collectors.toList());
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }
}