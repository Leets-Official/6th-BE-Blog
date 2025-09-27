package leets.blogapplication.service;

import leets.blogapplication.model.Post;
import leets.blogapplication.repository.InMemoryPostRepository;
import leets.blogapplication.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    public List<Post> getAll() { return postRepository.findAll(); }
//
//    public Post getById(Long id) {
//        return postRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 ID: " + id));
//    }
//
//    public Post create(String title, String content) {
//        if (title.length() > 100) throw new IllegalArgumentException("제목은 100자 이하만 허용됩니다.");
//        return postRepository.create(title, content);
//    }
//
//    public Post update(Long id, String title, String content) {
//        return postRepository.update(id, title, content);
//    }
//
//    public void delete(Long id) {
//        postRepository.deleteById(id);
//    }
    public List<Post> getAllPosts() { return postRepository.findAll(); }
    public Post getPostById(String id) { return postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found")); }
    public Post createPost(Post post) { return postRepository.create(post); }
    public Post updatePost(String id, Post post) { return postRepository.update(id, post); }
    public void deletePost(String id) { postRepository.delete(id); }
    public List<Post> getPostByTitle(String title) { return postRepository.findByTitle(title); }
}