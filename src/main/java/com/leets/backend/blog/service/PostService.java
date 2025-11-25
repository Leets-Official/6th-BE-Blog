package com.leets.backend.blog.service;

import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.dto.PostDetailResponse;
import com.leets.backend.blog.dto.PostListResponse;
import com.leets.backend.blog.dto.PostSaveRequest;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import com.leets.backend.blog.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시물 생성
    @Transactional
    public PostDetailResponse createPost(PostSaveRequest request) {
        // 1. 로그인한 사용자 ID 가져오기 (로그인 안되어있으면 예외 발생)
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new SecurityException("로그인이 필요한 서비스입니다."));

        User writer = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        Post newPost = new Post();
        newPost.setTitle(request.getTitle());
        newPost.setContent(request.getContent());
        newPost.setUser(writer);

        Post savedPost = postRepository.save(newPost);

        return new PostDetailResponse(savedPost, true); // 작성자 본인이므로 true
    }

    // 모든 게시물 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(PostListResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 게시물 조회
    @Transactional(readOnly = true)
    public PostDetailResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // 2. 현재 로그인한 사용자 확인 (비로그인 상태일 수도 있음)
        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();

        // 3. 소유권 확인 (로그인 상태이며, 작성자 ID와 일치하면 true)
        boolean isOwner = currentUserId.isPresent() &&
                post.getUser().getId().equals(currentUserId.get());

        return new PostDetailResponse(post, isOwner);
    }

    // 수정
    @Transactional
    public PostDetailResponse updatePost(Long postId, PostSaveRequest request) {
        // 1. 로그인 확인
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new SecurityException("로그인이 필요한 서비스입니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // 2. 소유권 검증 (작성자와 현재 로그인한 유저가 다르면 예외)
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("게시물을 수정할 권한이 없습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return new PostDetailResponse(post, true);
    }

    // 삭제
    @Transactional
    public void deletePost(Long postId) {
        // 1. 로그인 확인
        Long currentUserId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new SecurityException("로그인이 필요한 서비스입니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // 2. 소유권 검증
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}