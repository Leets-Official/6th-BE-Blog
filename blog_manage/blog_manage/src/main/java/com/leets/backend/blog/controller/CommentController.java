package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.config.CustomUserDetails;
import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 생성", description = "특정 게시글에 새로운 댓글을 작성합니다.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@PathVariable Long postId,
                                                                      @Valid @RequestBody CommentCreateRequest request,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponse response = commentService.createComment(postId, request, userDetails.getUser());
        return ResponseEntity.created(URI.create("/api/comments/" + response.getCommentId()))
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.findCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글의 내용을 수정합니다. 본인만 수정할 수 있습니다.")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable Long commentId,
                                                                      @Valid @RequestBody CommentUpdateRequest request,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponse response = commentService.updateComment(commentId, request, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다. 본인만 삭제할 수 있습니다.")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
