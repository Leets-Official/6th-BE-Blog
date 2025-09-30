package com.leets.backend.blog.controller;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class PostController{
    private final PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }
    //글 목록
    @GetMapping("/posts")
    public String posts(Model model){
        List<Post> posts=postService.getPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }

    //새 글 작성하기
    @GetMapping("/post/new")
    public String newPostForm(Model model){
        model.addAttribute("postForm", new PostForm());
        return "new-post";
    }
    @PostMapping("/post/new")
    public String create(@Valid @ModelAttribute("postForm") PostForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-post";
        }
        postService.create(form.getTitle(), form.getContent());
        return "redirect:/posts";
    }

    public static class PostForm {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;
        @NotBlank(message  = "내용은 필수입니다.")
        private String content;


        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}