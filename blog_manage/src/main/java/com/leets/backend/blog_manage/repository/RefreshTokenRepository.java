package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.RefreshToken;
import com.leets.backend.blog_manage.entity.User; // [추가]
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // [추가]

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token); // 토큰 값으로 찾는 경우
}