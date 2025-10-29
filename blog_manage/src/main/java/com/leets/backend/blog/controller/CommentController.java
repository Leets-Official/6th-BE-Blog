package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.ApiResponse;
import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment API", description = "댓글 관련 API")
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId, @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.createComment(postId, request);
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.CREATED, "댓글 생성 완료", response),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequest request
    ) {
        CommentResponse response = commentService.updateComment(postId, commentId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "댓글 수정 완료", response));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId, @PathVariable Long commentId
    ) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>(
                ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "댓글 삭제 완료",null),
                HttpStatus.NO_CONTENT
        );
    }
}
