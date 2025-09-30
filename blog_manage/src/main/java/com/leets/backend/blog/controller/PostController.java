package com.leets.backend.blog.controller;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회
    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts";
    }

    // 새로운 글 작성 폼
    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "new-post";
    }

    // 새로운 글 작성 저장
    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/posts"; // 작성 후 목록으로 리다이렉트
    }
}
