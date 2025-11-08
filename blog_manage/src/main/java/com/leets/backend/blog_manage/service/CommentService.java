package com.leets.backend.blog_manage.service;

import com.leets.backend.blog_manage.dto.CommentCreateRequest;
import com.leets.backend.blog_manage.dto.CommentResponse;
import com.leets.backend.blog_manage.dto.CommentUpdateRequest;
import com.leets.backend.blog_manage.entity.Comment;
import com.leets.backend.blog_manage.entity.Post;
import com.leets.backend.blog_manage.entity.User;
import com.leets.backend.blog_manage.exception.CustomException;
import com.leets.backend.blog_manage.exception.ErrorCode;
import com.leets.backend.blog_manage.repository.CommentRepository;
import com.leets.backend.blog_manage.repository.PostRepository;
import com.leets.backend.blog_manage.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder; // [추가]
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // --- [수정] ---
    // SecurityContext에서 인증된 사용자 정보를 가져오는 메소드
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // ID로 게시물 조회 (공용)
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    // ID로 댓글 조회 (공용)
    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    // 1. 댓글 생성
    @Transactional
    public CommentResponse createComment(Long postId, CommentCreateRequest request) {
        User user = getAuthenticatedUser(); // [수정]
        Post post = findPostById(postId);

        Comment comment = new Comment(request.getContent(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(savedComment);
    }

    // 2. 특정 게시물의 댓글 목록 조회 (로그인 불필요)
    // (이 메소드는 인증이 필요 없으므로 수정 사항 없음)
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = findPostById(postId);
        List<Comment> comments = post.getComments();
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정 (본인만 가능)
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        User user = getAuthenticatedUser(); // [수정]
        Comment comment = findCommentById(commentId);

        // [수정]
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        comment.update(request.getContent());
        return new CommentResponse(comment);
    }

    // 4. 댓글 삭제 (본인만 가능)
    @Transactional
    public void deleteComment(Long commentId) {
        User user = getAuthenticatedUser(); // [수정]
        Comment comment = findCommentById(commentId);

        // [수정]
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        commentRepository.delete(comment);
    }
}