package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
           select c from Comment c
           join fetch c.user u
           where c.post.postId = :postId
           order by c.createdAt asc
           """)
    List<Comment> findAllByPostIdWithUserOrderByCreatedAtAsc(@Param("postId") Long postId);

    @Query("""
           select c from Comment c
           join fetch c.user u
           where c.commentId = :commentId
           """)
    Optional<Comment> findByIdWithUser(@Param("commentId") Long commentId);
}
