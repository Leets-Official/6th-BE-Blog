package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUser_UserId(Long userId);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser_UserId(Long userId);
}
