package com.leets.backend.blog.service;


import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.repository.PostRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class PostService {
    private final PostRepository repository;


    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    //public List<Post> getAllPosts() {
    //    return repository.findAll();
    //}
//
//
    //public Post getPost(Long id) {
    //    return repository.findById(id);
    //}
//
//
    //public void createPost(String title, String content) {
    //    Post post = new Post(null, title, content);
    //    repository.save(post);
    //}
//
//
    //public Post updatePost(Long id, String title, String content) {
    //    if (repository.existsById(id)){
    //        Post post = repository.findById(id);
    //        post.setTitle(title);
    //        post.setContent(content);
    //        return repository.save(post);
    //    }
    //    else
    //        return null;
    //}
//
//
    //public void deletePost(Long id) {
    //    repository.deleteById(id);
    //}
}