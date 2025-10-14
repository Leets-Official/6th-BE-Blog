package leets.blogapplication.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "detail", nullable = false)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    protected Comment() { }

    public static Comment create(String detail, Post post, User user, List<Image> images) {
        Comment c = new Comment();
        c.detail = Objects.requireNonNull(detail, "detail");
        c.setPost(post);
        c.setUser(user);
        if (images != null) {
            for (Image img : images) {
                c.addImage(img); // 연관관계 주인(Image)까지 세팅
            }
        }
        return c;
    }

    public static Comment create(String detail, Post post, User user) {
        return create(detail, post, user, null);
    }

    public void addImage(Image image) {
        images.add(image);
        image.setComment(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setComment(null);
    }

    public void setPost(Post post) {
        this.post = Objects.requireNonNull(post, "post");
    }
    public void setUser(User user) {
        this.user = Objects.requireNonNull(user, "user");
    }
}
