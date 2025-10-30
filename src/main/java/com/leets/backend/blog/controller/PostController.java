package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.PostDetailResponse;
import com.leets.backend.blog.dto.PostListResponse;
import com.leets.backend.blog.dto.PostSaveRequest;
import com.leets.backend.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDetailResponse> createPost(@Valid @RequestBody PostSaveRequest request) {
        PostDetailResponse response = postService.createPost(1L, request); // 임시 userId
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts() {
        List<PostListResponse> response = postService.findAllPosts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostById(@PathVariable Long postId) {
        PostDetailResponse response = postService.findPostById(1L, postId); // 임시 userId
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> updatePost(@PathVariable Long postId, @Valid @RequestBody PostSaveRequest request) {
        PostDetailResponse response = postService.updatePost(1L, postId, request); // 임시 userId
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(1L, postId); // 임시 userId
        return ResponseEntity.noContent().build();
    }
}