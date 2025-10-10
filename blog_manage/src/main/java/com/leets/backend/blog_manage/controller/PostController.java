package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.common.ApiResponse;
import com.leets.backend.blog_manage.dto.PostCreateRequest;
import com.leets.backend.blog_manage.dto.PostDetailResponse;
import com.leets.backend.blog_manage.dto.PostListResponse;
import com.leets.backend.blog_manage.dto.PostUpdateRequest;
import com.leets.backend.blog_manage.entity.Post;
import com.leets.backend.blog_manage.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        Post savedPost = postService.createPost(request);
        return new ResponseEntity<>(
                ApiResponse.success("게시물이 성공적으로 생성되었습니다.", new PostDetailResponse(savedPost)),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> listPosts(@PageableDefault(size = 10) Pageable pageable) {
        Page<PostListResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success("게시물 목록 조회 성공", posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        return ResponseEntity.ok(ApiResponse.success("게시물 상세 조회 성공", new PostDetailResponse(post)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        postService.updatePost(id, request);
        Post updatedPost = postService.findPostById(id);
        return ResponseEntity.ok(ApiResponse.success("게시물이 성공적으로 수정되었습니다.", new PostDetailResponse(updatedPost)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("게시물이 성공적으로 삭제되었습니다.", null));
    }
}