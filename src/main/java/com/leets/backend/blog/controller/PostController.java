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

    // 새 글 작성 화면 띄우기
    @GetMapping("/post/new")
    public String newPostForm() {
        return "new-post"; // templates/new-post.html
    }

    // 새 글 저장 처리
    @PostMapping("/post/new")
    public String createNewPost(@ModelAttribute PostRequest request) {
        postService.save(request);
        return "redirect:/posts"; // 저장 후 목록으로 이동
    }

    // 글 목록 보기
    @GetMapping("/posts")
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts"; // templates/posts.html
    }
}
