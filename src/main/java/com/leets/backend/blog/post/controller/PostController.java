package com.leets.backend.blog.post.controller;

import com.leets.backend.blog.common.response.ApiResponse;
import com.leets.backend.blog.post.controller.dto.request.PostRequest;
import com.leets.backend.blog.post.controller.dto.request.PostUpdateRequest;
import com.leets.backend.blog.post.controller.dto.response.PostResponse;
import com.leets.backend.blog.post.controller.dto.response.PostSummaryResponse;
import com.leets.backend.blog.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostSummaryResponse>>> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts()));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostById(postId)));
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.created(postService.createPost(postRequest)));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @Valid @RequestBody PostUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(postService.updatePost(postId, userId, request)));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}


