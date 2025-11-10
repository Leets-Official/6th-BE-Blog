package com.leets.backend.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.backend.blog.domain.User; // 1. Optional 임포트 추가

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 2. 로그인 시 이메일로 사용자 조회를 위한 메서드 추가
    Optional<User> findByEmail(String email);

    // 3. 회원가입 시 이메일 중복 체크를 위한 메서드 추가
    boolean existsByEmail(String email);

    // 4. 회원가입 시 닉네임 중복 체크를 위한 메서드 추가
    boolean existsByNickname(String nickname);
}