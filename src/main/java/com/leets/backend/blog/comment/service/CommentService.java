package com.leets.backend.blog.comment.service;

import com.leets.backend.blog.comment.controller.dto.request.CommentRequest;
import com.leets.backend.blog.comment.controller.dto.response.CommentResponse;
import com.leets.backend.blog.comment.entity.Comment;
import com.leets.backend.blog.comment.repository.CommentRepository;
import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.post.entity.Post;
import com.leets.backend.blog.post.entity.User;
import com.leets.backend.blog.post.repository.PostRepository;
import com.leets.backend.blog.post.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 등록
    public CommentResponse createComment(CommentRequest commentRequest, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "이용자를 찾을 수 없습니다."));

        Comment comment = Comment.of(post, user, commentRequest.getContent());
        return CommentResponse.from(commentRepository.save(comment));
    }

    // 전체 댓글 목록 조회
    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 수정
    public CommentResponse updateComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS, "댓글 수정/삭제 권한이 없습니다.");
        }

        comment.updateContent(newContent);
        return CommentResponse.from(commentRepository.save(comment));
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS, "댓글 수정/삭제 권한이 없습니다."));

        if(!comment.getUser().getId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS, "댓글 수정/삭제 권한이 없습니다.");

        commentRepository.delete(comment);
    }
}
