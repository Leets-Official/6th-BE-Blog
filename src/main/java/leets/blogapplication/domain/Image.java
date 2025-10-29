package leets.blogapplication.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "image") // 실제 테이블명이 image면 명시
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void setPost(Post post) { this.post = post; }
    public void setComment(Comment comment) { this.comment = comment; }

    protected Image() {}
}

