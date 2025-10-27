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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private User getTemporaryUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public Post createPost(PostCreateRequest request) {
        User user = getTemporaryUser();
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
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(1L)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }
        post.update(request.getTitle(), request.getContent());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(1L)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }
        postRepository.delete(post);
    }
}