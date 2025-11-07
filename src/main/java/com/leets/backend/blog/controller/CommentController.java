package com.leets.backend.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentSaveRequest;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.service.CommentService;

// Swagger 어노테이션 임포트
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment API", description = "댓글 관련 API") // 1. @Tag 추가
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 새 댓글을 작성합니다.") // 2. @Operation 추가
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(
            @PathVariable Long postId,
            @RequestBody CommentSaveRequest request
    ) {
        CommentResponse response = commentService.save(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글별 댓글 조회", description = "특정 게시글에 달린 모든 댓글을 조회합니다.") // 2. @Operation 추가
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(
            @PathVariable Long postId
    ) {
        List<CommentResponse> responses = commentService.findAllByPost(postId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글의 내용을 수정합니다.") // 2. @Operation 추가
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        CommentResponse response = commentService.update(commentId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.") // 2. @Operation 추가
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}