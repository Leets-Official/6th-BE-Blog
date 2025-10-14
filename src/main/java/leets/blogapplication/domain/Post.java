package leets.blogapplication.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id") // ✅ 컬럼명 post_id 유지, 자바 필드는 id로 통일
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) // ✅ users_id → user_id 로 통일 (users 테이블의 PK: id)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    //return 안 해도 됨
    public static Post create(User user, String title, String content) {
        Post p = new Post();
        p.setUser(user);
        p.title = title;
        p.content = content;
        return p; // ← 편의상 반환해 두면 테스트/사용이 더 깔끔
    }

    public static Post create(User user, String title, String content, List<Image> images) {
        Post p = create(user, title, content);
        if (images != null) {
            for (Image img : images) {
                p.addImage(img);
            }
        }
        return p;
    }

    public Post() {}

    // ====== 접근자 ======
    // --- getters ---
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // --- setters ---
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ====== 편의 메서드 ======
    public void setUser(User user) {
        if (this.user != null) {
            this.user.getPosts().remove(this);
        }
        this.user = user;
        if (!user.getPosts().contains(this)) {
            user.getPosts().add(this);
        }
    }

    public void changeTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        // comment.setPost(this); // Comment.create/setPost 에서 처리하므로 여기선 생략 가능
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        // comment.setPost(null); // 필요 시 정합성 관리
    }
}
