package com.leets.backend.blog.repository;

import com.leets.backend.blog.dto.PostListResponseDTO;
import com.leets.backend.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("SELECT new com.leets.backend.blog.dto.PostListResponseDTO(p.postId, p.title, p.user.nickname, SIZE(p.comments), p.createdAt, p.updatedAt) " +
            "FROM Post p JOIN p.user " +
            "ORDER BY p.createdAt DESC") // 최신순 정렬
    Page<PostListResponseDTO> findAllAsListDTO(Pageable pageable);
}
