package com.leets.backend.blog.domain;

import com.leets.backend.blog.DTO.CommentRequestDTO;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments") // DB 테이블명
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "comment_id")
    private Long commentId;

    // 부모 댓글 ID (FK)
    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_comment_id") // FK 컬럼
    private List<Comment> childComments = new ArrayList<>();

    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 기본 생성자
    public Comment() {}

    public static Comment createComment(Post post, User user, CommentRequestDTO dto) {
        Comment comment = new Comment();

        comment.post = post;
        comment.user = user;
        comment.content = dto.getContent();
        comment.createDate = LocalDateTime.now();

        return comment;
    }

    public static Comment createChildComment(Post post, User user, Long parentCommentId, CommentRequestDTO dto) {
        Comment comment = new Comment();

        comment.post = post;
        comment.user = user;
        comment.parentCommentId = parentCommentId;
        comment.content = dto.getContent();
        comment.createDate = LocalDateTime.now();

        return comment;
    }

    public void updateComment(String content) {
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

    public Long getCommentId() {
        return commentId;
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

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }
}
