package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.*;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public PostDetailResponseDTO getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        User loginUser = findDummyUser();

        return PostDetailResponseDTO.from(post, loginUser);
    }

    // 게시물 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDTO> getPostList(int page) {

        // 1. Pageable 객체 생성 (page는 0부터 시작, 정렬 기준: createdAt 내림차순)
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 2. DTO 프로젝션을 사용한 메서드 호출
        Page<PostListResponseDTO> postPage = postRepository.findAllAsListDTO(pageable);

        // 3. Page<DTO>에서 List<DTO>를 반환
        return postPage.getContent();
    }

    // 게시물 수정
    public PostResponseDTO updatePost(Long postId, PostUpdateRequestDTO requestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.updatePost(requestDTO.getTitle(), requestDTO.getContent());

        return PostResponseDTO.from(post);
    }

    // 게시물 삭제
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
