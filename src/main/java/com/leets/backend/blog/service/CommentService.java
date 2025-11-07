package com.leets.backend.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.backend.blog.domain.Comment;
import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentSaveRequest;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.repository.CommentRepository;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;

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

    // 댓글 등록
    @Transactional
    public CommentResponse save(Long postId, CommentSaveRequest request, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));

        Comment comment = new Comment(request.getContent(), post, currentUser);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(savedComment, true);
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByPost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));

        return commentRepository.findByPost(post).stream()
                .map(comment -> {
                    boolean isOwner = comment.getUser().getId().equals(currentUserId);
                    return new CommentResponse(comment, isOwner);
                })
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    @Transactional
    public CommentResponse update(Long commentId, CommentUpdateRequest request, Long currentUserId) {
        Comment comment = findCommentAndCheckOwnership(commentId, currentUserId);
        comment.updateContent(request.getContent()); // (Comment 엔티티에 updateContent 메서드 필요)
        return new CommentResponse(comment, true);
    }

    // 4. 댓글 삭제
    @Transactional
    public void delete(Long commentId, Long currentUserId) {
        Comment comment = findCommentAndCheckOwnership(commentId, currentUserId);
        commentRepository.delete(comment);
    }

    //  댓글 조회 및 소유권 검증
    private Comment findCommentAndCheckOwnership(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("댓글 수정/삭제 권한이 없습니다."); 
        }
        return comment;
    }
}