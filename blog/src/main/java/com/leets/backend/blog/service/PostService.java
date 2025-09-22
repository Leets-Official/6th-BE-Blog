package com.leets.backend.blog.service;

import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return postRepository.getAllPosts();
    }

    public void createPost(String title, String content) {

        Post newPost = new Post(null, title, content);

        postRepository.save(newPost);
    }
}
