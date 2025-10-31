package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.common.ApiResponse;
import com.leets.backend.blog_manage.dto.PostCreateRequest;
import com.leets.backend.blog_manage.dto.PostDetailResponse;
import com.leets.backend.blog_manage.dto.PostListResponse;
import com.leets.backend.blog_manage.dto.PostUpdateRequest;
import com.leets.backend.blog_manage.entity.Post;
import com.leets.backend.blog_manage.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 (Post) API", description = "게시물 CRUD 관련 API")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "게시물 생성", description = "새로운 게시물을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        Post savedPost = postService.createPost(request);
        return new ResponseEntity<>(
                ApiResponse.success("게시물이 성공적으로 생성되었습니다.", new PostDetailResponse(savedPost)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 페이지별로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> listPosts(@PageableDefault(size = 10) Pageable pageable) {
        Page<PostListResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success("게시물 목록 조회 성공", posts));
    }

    @Operation(summary = "게시물 상세 조회", description = "특정 ID의 게시물을 상세 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        return ResponseEntity.ok(ApiResponse.success("게시물 상세 조회 성공", new PostDetailResponse(post)));
    }

    @Operation(summary = "게시물 수정", description = "특정 ID의 게시물을 수정합니다. (작성자 본인만 가능)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        postService.updatePost(id, request);
        Post updatedPost = postService.findPostById(id);
        return ResponseEntity.ok(ApiResponse.success("게시물이 성공적으로 수정되었습니다.", new PostDetailResponse(updatedPost)));
    }

    @Operation(summary = "게시물 삭제", description = "특정 ID의 게시물을 삭제합니다. (작성자 본인만 가능)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("게시물이 성공적으로 삭제되었습니다.", null));
    }
}