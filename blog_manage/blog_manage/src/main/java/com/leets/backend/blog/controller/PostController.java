package com.leets.backend.blog.controller;

import com.leets.backend.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller     // 요청/응답 처리 (웹 요청을 받아 Service 호출)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {        // Service 계층 주입(의존성 주입)
        this.postService = postService;
    }

    @GetMapping("/posts")       // 게시글 목록 조회(Read)
    public String listPosts(Model model) {
        return "posts";
    }

    @GetMapping("/post/new")        // 새 게시글 작성 폼
    public String newPostForm() {
        return "new-post";
    }

    @PostMapping("/posts")       // 새 게시글 생성(Create)
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content) {
        return "redirect:/posts";
    }
}
