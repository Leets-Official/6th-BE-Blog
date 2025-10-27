package com.leets.backend.blog.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.util.StringUtil;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailResponseDTO {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private List<CommentResponseDTO> comments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    private Boolean isMyPost;

    public PostDetailResponseDTO(Post post, User user, List<CommentResponseDTO> comments, Long currentUserId) {
        this.postId = post.getPostId();
        this.nickname = user.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.comments = comments;
        this.isMyPost = StringUtil.isNullOrEmpty(currentUserId.toString()) ? false :
                post.getUser().getUserId().equals(currentUserId);
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
    }

    public Long getPostId() {
        return postId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<CommentResponseDTO> getComments() {
        return comments;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public Boolean getMyPost() {
        return isMyPost;
    }
}
