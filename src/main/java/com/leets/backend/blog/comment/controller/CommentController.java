package com.leets.backend.blog.comment.controller;

import com.leets.backend.blog.comment.controller.dto.request.CommentRequest;
import com.leets.backend.blog.comment.controller.dto.response.CommentResponse;
import com.leets.backend.blog.comment.service.CommentService;
import com.leets.backend.blog.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService ) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
          @PathVariable Long postId,
          @RequestBody @Valid CommentRequest request
    ){
        // 더미 userId 사용
        Long dummyUserId = 1L;
        CommentResponse response = commentService.createComment(request, postId, dummyUserId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }

    // 전체 댓글 목록 조회 (추가)
    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ){
        Long dummyUserId = 1L;
        CommentResponse response = commentService.updateComment(commentId, dummyUserId, request.getContent());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ){
        Long dummyUserId = 1L;
        commentService.deleteComment(commentId, dummyUserId);
        return ResponseEntity.noContent().build();
    }

}
