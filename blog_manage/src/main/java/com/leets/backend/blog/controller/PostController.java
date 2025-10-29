package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 전체 조회
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.findById(id);
    }

    // 게시글 작성 (PostCreateRequest 사용)
    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest request) {
        return postService.createPost(request);
    }

    // 게시글 수정 (PostUpdateRequest 사용)
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostUpdateRequest request) {
        return postService.updatePost(id, request);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
