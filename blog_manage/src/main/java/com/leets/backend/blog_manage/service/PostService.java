package com.leets.backend.blog_manage.service;

import com.leets.backend.blog_manage.dto.PostCreateRequest;
import com.leets.backend.blog_manage.dto.PostListResponse;
import com.leets.backend.blog_manage.dto.PostUpdateRequest;
import com.leets.backend.blog_manage.entity.Post;
import com.leets.backend.blog_manage.entity.User;
import com.leets.backend.blog_manage.exception.CustomException;
import com.leets.backend.blog_manage.exception.ErrorCode;
import com.leets.backend.blog_manage.repository.PostRepository;
import com.leets.backend.blog_manage.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시물 서비스
 * 게시물 CRUD 비즈니스 로직 처리
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /** 인증된 사용자 정보 조회 */
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /** 게시물 생성 */
    @Transactional
    public Post createPost(PostCreateRequest request) {
        User user = getAuthenticatedUser();
        Post post = request.toEntity(user);
        return postRepository.save(post);
    }

    /** 게시물 목록 조회 (페이징) */
    @Transactional(readOnly = true)
    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostListResponse::new);
    }

    /** 게시물 조회 */
    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    /** 게시물 수정 - 작성자 본인만 가능 */
    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {
        User user = getAuthenticatedUser();
        Post post = findPostById(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }
        post.update(request.getTitle(), request.getContent());
    }

    /** 게시물 삭제 - 작성자 본인만 가능 */
    @Transactional
    public void deletePost(Long postId) {
        User user = getAuthenticatedUser();
        Post post = findPostById(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }
        postRepository.delete(post);
    }
}