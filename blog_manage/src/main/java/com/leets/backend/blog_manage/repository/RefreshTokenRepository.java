package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.RefreshToken;
import com.leets.backend.blog_manage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 리프레시 토큰 리포지토리
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}