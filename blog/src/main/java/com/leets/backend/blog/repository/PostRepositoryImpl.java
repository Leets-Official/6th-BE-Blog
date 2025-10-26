package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Post> findPostsWithPaging(int offset, int limit) {
        // JPQL을 사용하여 페이징 처리 (최신순 정렬)
        return entityManager.createQuery(
                        "select p from Post p order by p.createdAt desc", Post.class)
                .setFirstResult(offset) // OFFSET 설정
                .setMaxResults(limit)   // LIMIT 설정
                .getResultList();
    }

    @Override
    public long countAllPosts() {
        // 전체 개수 조회
        return entityManager.createQuery("select count(p) from Post p", Long.class)
                .getSingleResult();
    }
}
