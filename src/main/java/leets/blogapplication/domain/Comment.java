package leets.blogapplication.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comId;

    @Column(name = "detail", nullable = false)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Comment() {
    }

    public static Comment create(String detail, Post post, User user) {
        Comment c = new Comment();
        c.detail = detail;
        c.post = post;
        c.user = user;
        return c;
    }
}
