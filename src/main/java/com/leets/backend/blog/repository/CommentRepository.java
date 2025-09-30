package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Comment;
import com.leets.backend.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
