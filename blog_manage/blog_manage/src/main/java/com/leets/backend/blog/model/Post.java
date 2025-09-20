package com.leets.backend.blog.model;

public class Post {     // 게시글 객체 (id, title, content 등)
    private Long id;
    private String title;
    private String content;

    public Post() {}

    public Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter와 Setter기능 구현
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
}
