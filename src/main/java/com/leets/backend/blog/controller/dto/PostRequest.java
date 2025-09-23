package com.leets.backend.blog.controller.dto;

public class PostRequest {
    private String title;
    private String content;

    public PostRequest() {}                 // 기본 생성자

    public String getTitle() {              // getter
        return title;
    }
    public void setTitle(String title) {    // setter
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
