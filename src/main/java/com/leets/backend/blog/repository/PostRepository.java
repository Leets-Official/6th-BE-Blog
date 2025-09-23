package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(sequence.incrementAndGet());
        }
        store.put(post.getId(), post);
        return post;
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Post> findAll() {
        List<Post> list = new ArrayList<>(store.values());
        list.sort(Comparator.comparing(Post::getId).reversed());
        return list;
    }
}
