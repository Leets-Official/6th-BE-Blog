package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    void deleteByUser(User user);
}
