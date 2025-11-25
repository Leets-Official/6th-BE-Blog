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
        // userId 파라미터 제거 (Service에서 SecurityUtil로 처리)
        PostDetailResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostListResponse>> getAllPosts() {
        List<PostListResponse> response = postService.findAllPosts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostById(@PathVariable Long postId) {
        // userId 파라미터 제거
        PostDetailResponse response = postService.findPostById(postId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> updatePost(@PathVariable Long postId, @Valid @RequestBody PostSaveRequest request) {
        // userId 파라미터 제거
        PostDetailResponse response = postService.updatePost(postId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        // userId 파라미터 제거
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}