package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthor_IdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
}
