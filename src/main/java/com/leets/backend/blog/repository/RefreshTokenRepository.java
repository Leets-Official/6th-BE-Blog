package com.leets.backend.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.backend.blog.domain.RefreshToken;

/**
 * RefreshToken을 DB에서 관리하기 위한 리포지토리
 * RefreshToken 엔티티의 ID (PK) 타입이 Long (userId)이므로 JpaRepository<RefreshToken, Long>
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // JpaRepository가 기본으로 제공하는 메서드들을 사용합니다.
    // (e.g., save, findById, deleteById)

    /**
     * 토큰 재발급(reissue) 시, 토큰 값(문자열)으로 DB를 조회하기 위해 이 메서드가 필요합니다.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * [추가]
     * 로그인 시, 사용자 ID(FK)로 기존 토큰이 있는지 조회하기 위해 이 메서드가 필요합니다.
     */
    Optional<RefreshToken> findByUserId(Long userId);
}