package com.leets.backend.blog.controller;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String getPosts(Model model) {
        List<Post> result = postService.getPosts();

        for(Post p: result) {
            System.out.println(p.getId());
        }

        model.addAttribute("posts", result);
        return "posts";
    }

    @GetMapping("/post/new")
    public String newPost() {
        return "new-post";
    }

    @PostMapping("/post/new")
    public String createPost(@RequestParam String title, @RequestParam String content) {

        postService.createPost(title, content);
        return "redirect:/posts";
    }
}
