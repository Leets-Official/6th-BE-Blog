package com.leets.backend.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDetailResponseDTO {

    private Long postId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
    private AuthorInfoDTO authorInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LoginUserInfoDTO loginUserInfo;
    private List<CommentResponseDTO> comments;

    public PostDetailResponseDTO() { }

    public static PostDetailResponseDTO from(Post post, User loginUser) {
        PostDetailResponseDTO responseDTO = new PostDetailResponseDTO();

        responseDTO.postId = post.getPostId();
        responseDTO.title = post.getTitle();
        responseDTO.content = post.getContent();
        responseDTO.createdAt = post.getCreatedAt();
        responseDTO.updatedAt = post.getUpdatedAt();
        responseDTO.authorInfo = AuthorInfoDTO.from(post.getUser());

        // loginUser가 null이 아닐 때만 loginUserInfo를 설정
        if (loginUser != null) {
            responseDTO.loginUserInfo = LoginUserInfoDTO.from(loginUser);
        } else {
            responseDTO.loginUserInfo = null;
        }

        responseDTO.comments = post.getComments().stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());

        return responseDTO;
    }

    // getters
    public Long getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public AuthorInfoDTO getAuthorInfo() { return authorInfo; }
    public LoginUserInfoDTO getLoginUserInfo() { return loginUserInfo; }
    public List<CommentResponseDTO> getComments() { return comments; }
}
