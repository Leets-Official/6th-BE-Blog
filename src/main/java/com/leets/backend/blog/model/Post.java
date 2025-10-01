package com.leets.backend.blog.model;

public class Post {
    private Long id;
    private String title;
    private String content;

    public Post(String title, String content) {
        this.id = null;  // 처음 생성할 때는 아직 ID가 없으므로 null
        this.title = title;
        this.content = content;
    }

    // Getter 메서드들
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // Setter 메서드 (특히 id는 Repository에서 설정해줄 수 있음)
    public void setId(Long id) {
        this.id = id;
    }
}
