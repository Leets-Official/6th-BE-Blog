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

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(
            @PathVariable Long postId,
            @RequestBody CommentSaveRequest request
    ) {
        Long currentUserId = 1L; 
        CommentResponse response = commentService.save(postId, request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(
            @PathVariable Long postId
    ) {
        Long currentUserId = 1L; // !! 임시 하드코딩.
        List<CommentResponse> responses = commentService.findAllByPost(postId, currentUserId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        Long currentUserId = 1L; // !! 임시 하드코딩.
        CommentResponse response = commentService.update(commentId, request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        Long currentUserId = 1L; // !! 임시 하드코딩.
        commentService.delete(commentId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}