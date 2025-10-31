package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponseDto;
import com.leets.backend.blog.entity.Comment;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.exception.CommentNotFoundException;
import com.leets.backend.blog.exception.CommentPermissionException;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.repository.CommentRepository;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Create
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateRequest req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(req.content());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.from(saved);
    }

    // Read (비로그인 조회 가능)
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdWithUserOrderByCreatedAtAsc(postId);
        return comments.stream().map(CommentResponseDto::from).toList();
    }

    // Update (본인만)
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequest req, Long userId) {
        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CommentPermissionException(userId, commentId);
        }

        comment.setContent(req.content());
        comment.setUpdatedAt(LocalDateTime.now());

        // JPA 변경감지로 flush 시 업데이트 반영
        return CommentResponseDto.from(comment);
    }

    // Delete (본인만)
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CommentPermissionException(userId, commentId);
        }

        commentRepository.delete(comment);
    }
}