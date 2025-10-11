package com.leets.backend.blog.domain;

import com.leets.backend.blog.DTO.PostRequestDTO;
import com.leets.backend.blog.util.StringUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts") // DB 테이블명
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    // 게시물 내용을 HTML 소스 자체로 저장(이미지도 img태그로 저장)
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    // 기본 생성자
    public Post() {}

    public static Post createPost(PostRequestDTO dto, User user) {
        Post post = new Post();
        post.title = dto.getTitle();
        post.content = dto.getContent();
        post.createDate = LocalDateTime.now();
        post.user = user;

        return post;
    }

    public void updatePost(String title, String content){
        this.title = StringUtil.isNullOrEmpty(title) ? this.title : title;
        this.content = StringUtil.isNullOrEmpty(content) ? this.content : content;
        this.updateDate = LocalDateTime.now();
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public User getUser() {
        return user;
    }
}
