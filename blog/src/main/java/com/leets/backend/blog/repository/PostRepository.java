package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private final AtomicLong postId = new AtomicLong(0);

    private final HashMap<Long, Post> store = new HashMap<>();

    public void save(Post post) {
        post.setId(postId.incrementAndGet());
        store.put(post.getId(), post);
    }

    public List<Post> getAllPosts() {
        Collection<Post> values = store.values();
        List<Post> result = new ArrayList<>(values);
        return result;
    }
}