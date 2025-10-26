package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.PostCreateRequestDTO;
import com.leets.backend.blog.dto.PostResponseDTO;
import com.leets.backend.blog.dto.PostUpdateRequestDTO;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final String DUMMY_USER_EMAIL = "dummy@naver.com";
    private static final int PAGE_SIZE = 10;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시물 생성
    @Transactional
    public PostResponseDTO createPost(PostCreateRequestDTO requestDTO) {

        User user = findDummyUser();
        Post post = Post.createPost(
                requestDTO.getTitle(), requestDTO.getContent(), user
        );
        Post savedPost = postRepository.save(post);

        return PostResponseDTO.from(savedPost);
    }

    // 게시물 상세 조회
    @Transactional(readOnly = true)
    public PostResponseDTO getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return PostResponseDTO.from(post);
    }

    // 게시물 목록 조회
    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPostList(int page) {

        // offset 계산
        int offset = page * PAGE_SIZE;

        List<Post> posts = postRepository.findPostsWithPaging(offset, PAGE_SIZE);

        long totalCount = postRepository.countAllPosts();

        List<PostResponseDTO> dtoList = posts.stream()
                .map(PostResponseDTO::from)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, PageRequest.of(page, PAGE_SIZE), totalCount);
    }

    // 게시물 수정
    @Transactional
    public PostResponseDTO updatePost(Long postId, PostUpdateRequestDTO requestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.updatePost(requestDTO.getTitle(), requestDTO.getContent());

        return PostResponseDTO.from(post);
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        postRepository.delete(post);
    }

    // 더미 유저 생성
    private User createDummyUser() {
        User user = new User();
        return userRepository.save(user.createDummy());
    }

    // 더미 유저 찾기
    private User findDummyUser() {
        User user = userRepository.findByEmail(DUMMY_USER_EMAIL);
        if(user == null) {
            user = createDummyUser();
        }
        return user;
    }
}
