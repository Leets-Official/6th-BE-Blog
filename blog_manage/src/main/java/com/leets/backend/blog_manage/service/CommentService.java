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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    // PostService와 동일한 임시 사용자 조회 로직

    private User getTemporaryUser() {
        // 요구사항: 로그인 기능 구현 전이므로 임시 사용자(ID 1L) 사용
        return userRepository.findById(1L)
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
        User user = getTemporaryUser(); // 임시 유저 사용
        Post post = findPostById(postId); // 댓글을 달 게시물 조회

        Comment comment = new Comment(request.getContent(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(savedComment);
    }

    // 2. 특정 게시물의 댓글 목록 조회 (로그인 불필요)
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        // 게시물이 존재하는지 먼저 확인
        Post post = findPostById(postId);

        // 게시물 엔티티에서 직접 댓글 목록을 가져옴 (JPA 연관관계 활용)
        List<Comment> comments = post.getComments();

        // Comment 엔티티 리스트를 CommentResponse DTO 리스트로 변환
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정 (본인만 가능)
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        User user = getTemporaryUser(); // 임시 유저 (ID: 1L)
        Comment comment = findCommentById(commentId);

        // 본인 확인 (임시 유저 ID 1L 기준)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        comment.update(request.getContent());
        // @Transactional 어노테이션으로 인해 'Dirty checking'이 발생하므로
        // commentRepository.save(comment)를 명시적으로 호출할 필요 없음.

        return new CommentResponse(comment);
    }

    // 4. 댓글 삭제 (본인만 가능)
    @Transactional
    public void deleteComment(Long commentId) {
        User user = getTemporaryUser(); // 임시 유저 (ID: 1L)
        Comment comment = findCommentById(commentId);

        // 본인 확인 (임시 유저 ID 1L 기준)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        commentRepository.delete(comment);
    }
}