package com.leets.backend.blog.controller;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService = new PostService();

    @GetMapping("/post")
    public String getAllPosts(Model model) {
        return "postList"; // postList.html 템플릿을 렌더링
    }

    @GetMapping("/post/new")
    public String newPostForm() {
        return "postForm"; // postForm.html 템플릿을 렌더링 (게시글 작성 폼)
    }

    @PostMapping("/post/new")
    public String createPost(@RequestParam String title, @RequestParam String content) {
        return "redirect:/posts"; // 게시글 작성 후 목록 페이지로 리다이렉트
    }

    @GetMapping("/post/{id}")
    public ModelAndView getPostById(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("postDetail"); // postDetail.html 템플릿을 렌더링
        return mav;
    }
}
