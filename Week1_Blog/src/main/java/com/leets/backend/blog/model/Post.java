package com.leets.backend.blog.model;


public class Post {
    private Long id;
    private String title;
    private String content;

    public Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


    public Long getId() { return id; }
    //public void setId(Long id) { this.id = id; } 피드백 반영하여 제거


    public String getTitle() { return title; }
    //public void setTitle(String title) { this.title = title; } 피드백 반영하여 제거


    public String getContent() { return content; }
    //public void setContent(String content) { this.content = content; } 피드백 반영하여 제거


    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}