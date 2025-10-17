package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostResponse;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        Post post = postService.createPost(request);
        PostResponse response = new PostResponse(post);
        return ResponseEntity.created(URI.create("/api/posts/" + post.getPostId()))
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        List<PostResponse> responses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        PostResponse response = new PostResponse(post);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        Post post = postService.updatePost(id, request);
        PostResponse response = new PostResponse(post);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
