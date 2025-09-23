package com.leets.backend.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    // 생성자를 통한 의존성 주입 (DI)
    //@Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 게시글 저장 로직
     */
    public Post savePost(Post post) {
        // 비즈니스 로직 추가 가능 (예: 제목, 내용 비어있는지 검증)
        return postRepository.save(post);
    }

    /**
     * 모든 게시글 조회 로직
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}