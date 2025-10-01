package leets.blogapplication.service;

import leets.blogapplication.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    public List<PostM> getAll() { return postRepository.findAll(); }
//
//    public PostM getById(Long id) {
//        return postRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 ID: " + id));
//    }
//
//    public PostM create(String title, String content) {
//        if (title.length() > 100) throw new IllegalArgumentException("제목은 100자 이하만 허용됩니다.");
//        return postRepository.create(title, content);
//    }
//
//    public PostM update(Long id, String title, String content) {
//        return postRepository.update(id, title, content);
//    }
//
//    public void delete(Long id) {
//        postRepository.deleteById(id);
//    }
//    public List<PostM> getAllPosts() { return postRepository.findAll(); }
//    public PostM getPostById(String id) { return postRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("PostM not found")); }
//    public PostM createPost(PostM post) { return postRepository.create(post); }
//    public PostM updatePost(String id, PostM post) { return postRepository.update(id, post); }
//    public void deletePost(String id) { postRepository.delete(id); }
//    public List<PostM> getPostByTitle(String title) { return postRepository.findByTitle(title); }
}