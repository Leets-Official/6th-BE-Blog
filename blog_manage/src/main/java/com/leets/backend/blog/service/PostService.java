package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Transactional
    public Post createPost(PostCreateRequest request) {
        User user = findOrCreateDummyUser();
        Post post = new Post(user, request.getTitle(), request.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, PostUpdateRequest request) {
        Post post = findById(id);
        User dummyUser = findOrCreateDummyUser();

        if (!post.getUser().getUserId().equals(dummyUser.getUserId())) {
            throw new IllegalArgumentException("본인 게시글만 수정할 수 있습니다.");
        }

        post.updatePost(request.getTitle(), request.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findById(id);
        User dummyUser = findOrCreateDummyUser();

        if (!post.getUser().getUserId().equals(dummyUser.getUserId())) {
            throw new IllegalArgumentException("본인 게시글만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    private User findOrCreateDummyUser() {
        User user = userRepository.findByEmail(DUMMY_EMAIL);
        if (user == null) {
            user = User.createDummy();
            return userRepository.save(user);
        }
        return user;
    }
}
