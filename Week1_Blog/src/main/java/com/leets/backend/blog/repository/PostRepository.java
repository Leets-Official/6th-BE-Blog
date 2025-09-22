package com.leets.backend.blog.repository;


import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class PostRepository {
    private final Map<Long, Post> storage = new HashMap<>();
    private static long idGenerator = 1;


    public List<Post> findAll() {
        return new ArrayList<>(storage.values());
    }


    public Post findById(Long id) {
        return storage.get(id);
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            Long id = idGenerator++;
            post.setId(id);
        }
        storage.put(post.getId(), post);
        return post;
    }


    public void deleteById(Long id) {
        storage.remove(id);
    }


    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}