package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostResponse;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final String DUMMY_EMAIL = "dummy@naver.com";

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시물 목록 조회
    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    // 게시물 상세 조회
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostResponse.from(post);
    }

    // 게시물 생성
    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        User user = findOrCreateDummyUser();
        Post post = new Post(user, request.getTitle(), request.getContent());
        Post saved = postRepository.save(post);
        return PostResponse.from(saved);
    }

    // 게시물 수정
    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        User dummyUser = findOrCreateDummyUser();
        if (!post.getUser().getUserId().equals(dummyUser.getUserId())) {
            throw new IllegalArgumentException("본인 게시글만 수정할 수 있습니다.");
        }

        post.updatePost(request.getTitle(), request.getContent());
        return PostResponse.from(postRepository.save(post));
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        User dummyUser = findOrCreateDummyUser();
        if (!post.getUser().getUserId().equals(dummyUser.getUserId())) {
            throw new IllegalArgumentException("본인 게시글만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    private User findOrCreateDummyUser() {
        return userRepository.findByEmail(DUMMY_EMAIL)
                .orElseGet(() -> userRepository.save(User.createDummy()));
    }
}
