package com.leets.backend.blog.dto;

public class CommentUpdateRequest {

    private String content;

    // JSON 역직렬화를 위한 기본 생성자
    public CommentUpdateRequest() {
    }

    // Getter
    public String getContent() {
        return content;
    }

    // Setter
    public void setContent(String content) {
        this.content = content;
    }
}