package leets.blogapplication.repository;

import leets.blogapplication.controller.PostController;
import leets.blogapplication.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
    Page<Post> findByTitle(@Param("title") String title, Pageable pageable);

    @Query("UPDATE Post p SET p.title = :title, p.content = :content, p.updatedAt = :updatedAt WHERE p.id = :id")
    void updatePost(@Param("title") String title, @Param("content") String content,
                    @Param("updatedAt") LocalDateTime updatedAt, @Param("id") Long id);

    Page<Post> findAll(Pageable pageable);
}