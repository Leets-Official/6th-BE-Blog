package leets.blogapplication.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;               // 부모 댓글 (NULL이면 최상위 댓글)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();  // 자식 댓글 리스트

    public void addChild(Comment child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    protected Comment() { }

    public static Comment create(String content, Post post, User user, Comment parent) {
        Comment c = new Comment();
        c.setContent(content);
        c.setPost(post);
        c.setUser(user);
        if(parent != null) {
            c.setParent(parent);
            parent.addChild(c);
        }
        return c;
    }

    public void setPost(Post post) { this.post = post; }
    public void setUser(User user) { this.user = user; }
    public void setContent(String content) { this.content = content; }
    public Long getId() { return id; }
    public String getContent() { return content; }
    public Post getPost() { return post; }
}
