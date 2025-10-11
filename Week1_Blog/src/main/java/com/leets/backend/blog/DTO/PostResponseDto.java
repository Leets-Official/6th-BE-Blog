package com.leets.backend.blog.DTO;

import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.util.StringUtil;

import java.time.LocalDateTime;
import java.util.List;

// 전체 게시물 조회 DTO
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String nickname;
    private String profileImgUrl;
    private Integer commentCount;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String thumbnailUrl;
    private Boolean isMyPost;

    public PostResponseDto(Post post, User user, Long currentUserId) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfileImgUrl();
        this.commentCount = post.getComments().size();
        this.createdDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
        this.isMyPost = StringUtil.isNullOrEmpty(currentUserId.toString()) ? false : user.getUserId().equals(currentUserId);
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

    public String getNickname() {
        return nickname;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Boolean getMyPost() {
        return isMyPost;
    }
}
