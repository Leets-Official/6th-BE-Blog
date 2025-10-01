package com.leets.backend.blog.controller;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class PostController {
    private final PostService service;


    public PostController(PostService service) {
        this.service = service;
    }


    //@GetMapping("/posts")
    //public String listPosts(Model model) {
    //    model.addAttribute("posts", service.getAllPosts());
    //    return "posts";
    //}
//
//
    //@GetMapping("post/new")
    //public String newPostForm() {
    //    return "new-post";
    //}
//
//
    //@PostMapping("/new")
    //public String createPost(@RequestParam String title, @RequestParam String content) {
    //    service.createPost(title, content);
    //    return "redirect:/posts";
    //}
//
//
    //// 글 상세보기
    //@GetMapping("/post/{id}")
    //public String viewPost(@PathVariable("id") Long id, Model model) {
    //    Post post = service.getPost(id);
    //    if (post == null) {
    //        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    //    }
    //    model.addAttribute("post", post);
    //    return "view-post";
    //}
//
//
    //// 수정 폼
    //@GetMapping("/post/{id}/edit")
    //public String editForm(@PathVariable("id") Long id, Model model) {
    //    Post post = service.getPost(id);
    //    if (post == null) {
    //        return "redirect:/posts"; // 또는 404
    //    }
    //    model.addAttribute("post", post);
    //    return "edit-post";
    //}
//
//
    //// 수정 처리
    //@PostMapping("/post/{id}/edit")
    //public String updatePost(@PathVariable("id") Long id,
    //                         @RequestParam String title,
    //                         @RequestParam String content) {
    //    Post updated = service.updatePost(id, title, content);
    //    if (updated == null) {
    //        return "redirect:/posts";
    //    }
    //    return "redirect:/post/" + id;
    //}
//
//
    //// 삭제
    //@PostMapping("/post/{id}/delete")
    //public String deletePost(@PathVariable("id") Long id) {
    //    service.deletePost(id);
    //    return "redirect:/posts";
    //}
}
