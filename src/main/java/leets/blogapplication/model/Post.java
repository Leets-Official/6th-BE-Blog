package leets.blogapplication.model;

import java.time.LocalDateTime;

public class Post {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 최초에는 null, 수정 시에만 값 셋팅

    public Post() { }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //getters/setters
    public String getId() { return id; }
    public Post setId(String id) { this.id = id; return this;}
    public String getTitle() { return title; }
    public Post setTitle(String title) { this.title = title; return this; }
    public String getContent() { return content; }
    public Post setContent(String content) { this.content = content; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Post setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Post setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
}
