package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.config.CustomUserDetails;
import com.leets.backend.blog.dto.PostDetailResponse;
import com.leets.backend.blog.dto.PostListResponse;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<PostDetailResponse>> createPost(@Valid @RequestBody PostCreateRequest request,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        Post post = postService.createPost(request, userDetails.getUser());
        PostDetailResponse response = new PostDetailResponse(post);
        return ResponseEntity.created(URI.create("/api/posts/" + post.getPostId()))
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostListResponse>>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        List<PostListResponse> responses = posts.stream()
                .map(PostListResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostById(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        PostDetailResponse response = new PostDetailResponse(post);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> updatePost(@PathVariable Long id, 
                                                                      @Valid @RequestBody PostUpdateRequest request,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        Post post = postService.updatePost(id, request, userDetails.getUser());
        PostDetailResponse response = new PostDetailResponse(post);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
