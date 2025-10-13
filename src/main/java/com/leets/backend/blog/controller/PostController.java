package com.leets.backend.blog.controller;

import com.leets.backend.blog.controller.dto.PostRequest;
import com.leets.backend.blog.controller.dto.PostResponse;
import com.leets.backend.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회
    @GetMapping("/posts")
    public String getPosts(Model model) {
        model.addAttribute("posts", null);
        return "posts";
    }

    // 게시글 저장
    @GetMapping("/post/new")
    public String newPostForm() {
        return "new-post";
    }

    @PostMapping("/post/new")
    public String createNewPost(@ModelAttribute PostRequest request) {
        return "redirect:/posts";
    }
}
