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
import org.springframework.security.core.context.SecurityContextHolder; //
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // SecurityContext에서 인증된 사용자 정보를 가져오는 메소드
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Post createPost(PostCreateRequest request) {
        User user = getAuthenticatedUser();
        Post post = request.toEntity(user);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostListResponse::new);
    }

    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {
        User user = getAuthenticatedUser();
        Post post = findPostById(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }
        post.update(request.getTitle(), request.getContent());
    }

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