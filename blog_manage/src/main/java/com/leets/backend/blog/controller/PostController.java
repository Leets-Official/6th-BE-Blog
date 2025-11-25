package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostResponse;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.service.PostService;
import com.leets.backend.blog.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post API", description = "게시물 관련 API")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 목록 조회
    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.findAll();
        return ResponseEntity.ok(
                ApiResponse.onSuccess(HttpStatus.OK, "게시물 목록 조회 완료", posts)
        );
    }

    // 게시물 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "특정 게시물을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostDetail(@PathVariable Long postId) {
        PostResponse response = postService.findById(postId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(HttpStatus.OK, "게시물 상세 조회 완료", response)
        );
    }

    // 게시물 생성
    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.createPost(request);
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.CREATED, "게시물 생성 완료", response),
                HttpStatus.CREATED
        );
    }

    // 게시물 수정
    @Operation(summary = "게시물 수정", description = "게시물 내용을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request
    ) {
        PostResponse response = postService.updatePost(id, request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(HttpStatus.OK, "게시물 수정 완료", response)
        );
    }

    // 게시물 삭제
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "게시물 삭제 완료", null),
                HttpStatus.NO_CONTENT
        );
    }
}
