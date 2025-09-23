package com.leets.backend.blog.service;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public Post create(String title, String content) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setCreatedAt(LocalDateTime.now());
        return postRepository.save(p);
    }
}
