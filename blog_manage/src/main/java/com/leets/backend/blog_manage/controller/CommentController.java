package com.leets.backend.blog_manage.controller;

import com.leets.backend.blog_manage.common.ApiResponse;
import com.leets.backend.blog_manage.dto.CommentCreateRequest;
import com.leets.backend.blog_manage.dto.CommentResponse;
import com.leets.backend.blog_manage.dto.CommentUpdateRequest;
import com.leets.backend.blog_manage.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "댓글 (Comment) API", description = "게시물에 대한 댓글 관련 API")
@RestController
@RequestMapping("/api") // /api 공통 경로
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 1. 댓글 생성
     * POST /api/posts/{postId}/comments
     */
    @Operation(summary = "댓글 생성", description = "특정 게시물(postId)에 새로운 댓글을 등록합니다.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request) {

        CommentResponse commentResponse = commentService.createComment(postId, request);
        return new ResponseEntity<>(
                ApiResponse.success("댓글이 성공적으로 생성되었습니다.", commentResponse),
                HttpStatus.CREATED
        );
    }

    /**
     * 2. 특정 게시물 댓글 목록 조회 (요구사항: 로그인 없이 조회 가능)
     * GET /api/posts/{postId}/comments
     */
    @Operation(summary = "댓글 목록 조회", description = "특정 게시물(postId)의 모든 댓글을 조회합니다.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPostId(
            @PathVariable Long postId) {

        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(ApiResponse.success("댓글 목록 조회 성공", comments));
    }

    /**
     * 3. 댓글 수정 (요구사항: 본인 댓글만 수정 가능)
     * PUT /api/comments/{commentId}
     */
    @Operation(summary = "댓글 수정", description = "특정 ID의 댓글(commentId)을 수정합니다. (작성자 본인만 가능)")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request) {

        CommentResponse commentResponse = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 수정되었습니다.", commentResponse));
    }

    /**
     * 4. 댓글 삭제 (요구사항: 본인 댓글만 삭제 가능)
     * DELETE /api/comments/{commentId}
     */
    @Operation(summary = "댓글 삭제", description = "특정 ID의 댓글(commentId)을 삭제합니다. (작성자 본인만 가능)")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 삭제되었습니다.", null));
    }
}