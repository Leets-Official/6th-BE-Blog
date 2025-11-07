package com.leets.backend.blog.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.leets.backend.blog.exception.CommentPermissionException;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.exception.CommentNotFoundException;
import com.leets.backend.blog.util.SecurityUtil; // SecurityUtil 임포트
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

    // getTemporaryUser() 메서드 삭제됨

    // 댓글 등록
    @Transactional
    public CommentResponse save(Long postId, CommentSaveRequest request) {
        // SecurityUtil을 사용하여 현재 사용자 ID를 가져옵니다.
        // 로그아웃 상태면 "로그인이 필요합니다." 예외 발생
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new CommentPermissionException("댓글 작성 권한이 없습니다. 로그인이 필요합니다."));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = new Comment(request.getContent(), post, currentUser);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(savedComment, true); // 내가 방금 썼으니 항상 true
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByPost(Long postId) {
        // SecurityUtil을 사용. 로그아웃 상태일 경우 Optional.empty() 반환
        Optional<Long> currentUserIdOptional = SecurityUtil.getCurrentUserId();
        // 로그아웃 상태면 null, 로그인 상태면 userId
        Long currentUserId = currentUserIdOptional.orElse(null);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return commentRepository.findByPost(post).stream()
                .map(comment -> {
                    // 로그인 상태이고(currentUserId != null), 댓글 작성자와 ID가 같으면 true
                    boolean isOwner = (currentUserId != null) &&
                            (Objects.equals(comment.getUser().getId(), currentUserId));
                    return new CommentResponse(comment, isOwner);
                })
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    @Transactional
    public CommentResponse update(Long commentId, CommentUpdateRequest request) {
        // 로그아웃 상태면 "로그인이 필요합니다." 예외 발생
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new CommentPermissionException("댓글 수정 권한이 없습니다. 로그인이 필요합니다."));

        Comment comment = findCommentAndCheckOwnership(commentId, currentUserId);
        comment.updateContent(request.getContent());

        return new CommentResponse(comment, true); // 수정 권한이 있으니 true
    }

    // 4. 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        // 로그아웃 상태면 "로그인이 필요합니다." 예외 발생
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new CommentPermissionException("댓글 삭제 권한이 없습니다. 로그인이 필요합니다."));

        Comment comment = findCommentAndCheckOwnership(commentId, currentUserId);
        commentRepository.delete(comment);
    }

    // 댓글 조회 및 소유권 검증
    private Comment findCommentAndCheckOwnership(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!Objects.equals(comment.getUser().getId(), currentUserId)) {
            // ID가 다르다면 (null 포함) 권한 예외 발생
            throw new CommentPermissionException(commentId, currentUserId);
        }
        return comment;
    }
}