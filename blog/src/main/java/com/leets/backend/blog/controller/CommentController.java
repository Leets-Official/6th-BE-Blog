package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.dto.CommentCreateRequestDTO;
import com.leets.backend.blog.dto.CommentResponseDTO;
import com.leets.backend.blog.dto.CommentUpdateRequestDTO;
import com.leets.backend.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment API", description = "댓글 관련 API")
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    
    private final CommentService commentService;
    
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    // 댓글 생성
    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
            @PathVariable Long postId, @Valid @RequestBody CommentCreateRequestDTO requestDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal String email
    ) {
        CommentResponseDTO responseDTO = commentService.createComment(postId, requestDTO, email);

        // 201 Created 반환
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.CREATED, "댓글 생성 완료", responseDTO),
                HttpStatus.CREATED
        );
    }
    
    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(
            @PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequestDTO requestDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal String email
    ) {
        CommentResponseDTO responseDTO = commentService.updateComment(postId, commentId, requestDTO, email);

        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "댓글 수정 완료", responseDTO));
    }
    
    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId, @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal String email
    ) {
        commentService.deleteComment(postId, commentId, email);

        // 204 No Content 반환
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "댓글 삭제 완료"),
                HttpStatus.NO_CONTENT
        );
    }
}
