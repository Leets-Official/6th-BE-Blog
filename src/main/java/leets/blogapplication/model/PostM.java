package leets.blogapplication.model;

import java.time.LocalDateTime;

public class PostM {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 최초에는 null, 수정 시에만 값 셋팅

    public PostM() { }

    public PostM(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //getters/setters
    public String getId() { return id; }
    public PostM setId(String id) { this.id = id; return this;}
    public String getTitle() { return title; }
    public PostM setTitle(String title) { this.title = title; return this; }
    public String getContent() { return content; }
    public PostM setContent(String content) { this.content = content; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public PostM setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public PostM setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
    //해당 계층은 동명의 domain layer의 존재로 명칭이 변경 및 기능의 변경으로 후에 업데이트될 예정입니다.
}
