package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.entity.Comment;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.CommentAccessDeniedException;
import com.leets.backend.blog.exception.CommentNotFoundException;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.CommentRepository;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private static final String DUMMY_USER_EMAIL = "dummy@naver.com";

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 생성
    public CommentResponse createComment(Long postId, CommentCreateRequest request) {
        User user = findOrCreateDummyUser();

        // 게시글 존재 확인
        Post post = findPostById(postId);

        Comment comment = Comment.createComment(request.getContent(), user, post);
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    // 댓글 수정
    public CommentResponse updateComment(Long postId, Long commentId, CommentUpdateRequest request) {
        User user = findOrCreateDummyUser();

        // 댓글 조회 및 검증 (게시글, 댓글 존재, 작성자 확인)
        Comment comment = validateCommentAccess(postId, commentId, user);

        comment.updateComment(request.getContent());
        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long postId, Long commentId) {
        User user = findOrCreateDummyUser();

        // 댓글 조회 및 검증 (게시글, 댓글 존재, 작성자 확인)
        Comment comment = validateCommentAccess(postId, commentId, user);

        commentRepository.delete(comment);
    }


    // 더미 유저가 없으면 생성하고, 있으면 반환
    private User findOrCreateDummyUser() {
        return userRepository.findByEmail(DUMMY_USER_EMAIL)
                .orElseGet(this::createDummyUser);
    }

    // 더미 유저를 DB에 생성 (이미 존재하면 중복 예외 방지)
    private User createDummyUser() {
        User dummy = User.createDummy();

        // 혹시 병렬 요청 등으로 이미 생성되어 있을 경우 방어
        return userRepository.findByEmail(DUMMY_USER_EMAIL)
                .orElseGet(() -> userRepository.save(dummy));
    }

    // 게시글 ID로 게시글을 찾아 반환하고, 없으면 예외 발생
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private Comment validateCommentAccess(Long postId, Long commentId, User user) {
        // 게시글 존재 확인
        findPostById(postId);

        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        // 댓글의 게시글 일치 여부 확인
        if (!comment.getPost().getPostId().equals(postId)) {
            throw new CommentNotFoundException(commentId);
        }

        // 작성자 검증
        checkCommentAuthor(comment, user);

        return comment;
    }

    // 댓글 작성자 검증
    private void checkCommentAuthor(Comment comment, User user) {
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new CommentAccessDeniedException();
        }
    }
}