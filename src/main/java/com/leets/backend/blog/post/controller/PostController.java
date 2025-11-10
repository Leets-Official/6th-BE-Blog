package com.leets.backend.blog.post.controller;

import com.leets.backend.blog.common.response.ApiResponse;
import com.leets.backend.blog.post.controller.dto.request.PostRequest;
import com.leets.backend.blog.post.controller.dto.request.PostUpdateRequest;
import com.leets.backend.blog.post.controller.dto.response.PostResponse;
import com.leets.backend.blog.post.controller.dto.response.PostSummaryResponse;
import com.leets.backend.blog.post.service.PostService;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.login.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser(); // 로그인된 유저 정보 추출
        PostResponse response = postService.createPost(request, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser(); // 현재 로그인된 유저
        PostResponse response = postService.updatePost(postId, request, user);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        postService.deletePost(postId, user);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}


