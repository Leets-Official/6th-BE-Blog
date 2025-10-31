package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.dto.*;
import com.leets.backend.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post API", description = "게시물 관련 API")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 생성
    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(
            @Valid @RequestBody PostCreateRequestDTO requestDTO
    ) {
        PostResponseDTO responseDTO = postService.createPost(requestDTO);

        // 201 Created 반환
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.CREATED, "게시물 생성 완료", responseDTO),
                HttpStatus.CREATED
        );
    }

    // 게시물 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 내용을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> getPostDetail(
            @PathVariable Long postId
    ) {
        PostDetailResponseDTO responseDTO = postService.getPostDetail(postId);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 상세 조회 완료", responseDTO));
    }

    // 게시물 목록 조회(페이징 : 10개)
    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostListResponseDTO>>> getPostList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        List<PostListResponseDTO> postPage = postService.getPostList(page);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 목록 조회 완료", postPage));
    }

    // 게시물 수정
    @Operation(summary = "게시물 수정", description = "게시물 내용을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDTO requestDTO
    ) {
        PostResponseDTO responseDTO = postService.updatePost(postId, requestDTO);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 수정 완료", responseDTO));
    }

    // 게시물 삭제
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);

        // 204 No Content 반환
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "게시물 삭제 완료"),
                HttpStatus.NO_CONTENT
        );
    }
}
