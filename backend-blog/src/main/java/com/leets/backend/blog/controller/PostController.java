package com.leets.backend.blog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;

@Controller
public class PostController {

    private final PostService postService;

    //@Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts); 
        return "posts"; 
    }

    @GetMapping("/post/new")
    public String newPostForm() {
        return "new-post"; 
    }

    @PostMapping("/post/new")
    public String createPost(Post post) { 
        postService.savePost(post);
        return "redirect:/posts"; 
    }
}