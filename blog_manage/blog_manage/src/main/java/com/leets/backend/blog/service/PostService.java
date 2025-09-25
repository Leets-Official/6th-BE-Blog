package com.leets.backend.blog.service;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service        // 비즈니스 로직 담당, Repository 호출
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {     // Spring이 자동으로 Repository Bean을 주입
        this.postRepository = postRepository;
    }

    public Post createPost(String title, String content) {      // 새 게시글 생성(Create)
        Post post = new Post(null, title, content);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {       // 모든 게시글 조회(Read)
        return postRepository.findAll();
    }

    public Optional<Post> getPost(Long id) {        // 단일 게시글 조회(Read)
        return postRepository.findById(id);
    }

}
