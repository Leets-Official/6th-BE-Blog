package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.dto.PostCreateRequestDTO;
import com.leets.backend.blog.dto.PostResponseDTO;
import com.leets.backend.blog.dto.PostUpdateRequestDTO;
import com.leets.backend.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostDetail(
            @PathVariable Long postId
    ) {
        PostResponseDTO responseDTO = postService.getPostDetail(postId);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 상세 조회 완료", responseDTO));
    }

    // 게시물 목록 조회(페이징 : 10개)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponseDTO>>> getPostList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        Page<PostResponseDTO> postPage = postService.getPostList(page);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 목록 조회 완료", postPage));
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDTO requestDTO
    ) {
        PostResponseDTO responseDTO = postService.updatePost(postId, requestDTO);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "게시물 수정 완료", responseDTO));
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
