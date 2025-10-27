package com.leets.backend.blog.DTO;

import com.leets.backend.blog.domain.Comment;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.util.StringUtil;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentResponseDTO {
    private Long commentId;
    private Long parentCommentId;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @NotBlank
    private String nickname;
    private String profileImgUrl;
    private Boolean isMyComment;

    public CommentResponseDTO(Comment comment, User user, Long currentUserId) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.parentCommentId = comment.getParentCommentId();
        this.postId = comment.getPost().getPostId();
        this.userId = user.getUserId();
        this.content = comment.getContent();
        this.createDate = comment.getCreateDate();
        this.updateDate = comment.getUpdateDate();
        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfileImgUrl();
        this.isMyComment = StringUtil.isNullOrEmpty(currentUserId.toString()) ? false :
                comment.getUser().getUserId().equals(currentUserId);
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public Boolean getMyComment() {
        return isMyComment;
    }
}
