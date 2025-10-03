package com.leets.backend.blog.domain;

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

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String title;

    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "postId")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "postId")
    private List<PostImg> postImgs = new ArrayList<>();

    // 기본 생성자
    public Post() {}

    public Post(String userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void updatePost(String title, String content){
        this.title = StringUtil.isNullOrEmpty(title) ? this.title : title;
        this.content = StringUtil.isNullOrEmpty(content) ? this.content : content;
        this.updateDate = LocalDateTime.now();
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
