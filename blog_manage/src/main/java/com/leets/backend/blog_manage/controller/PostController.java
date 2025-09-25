package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.model.Post;
import com.leets.backend.blog_manage.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // http://localhost:8080/posts
    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts"; // posts.html 뷰 반환
    }

    // http://localhost:8080/post/new
    @GetMapping("/post/new")
    public String newPostForm() {
        return "new-post"; // new-post.html 뷰 반환
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content) {
        Post post = new Post(null, title, content); // ID는 Repository에서 자동 생성
        postService.createPost(post);
        return "redirect:/posts"; // 게시글 목록 페이지로 리다이렉트
    }
}