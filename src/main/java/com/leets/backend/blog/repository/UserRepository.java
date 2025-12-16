
package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByNick(String nick);
    boolean existsByEmail(String email);

    Optional<User> findTopByOrderByIdAsc();
    Optional<User> findByKakaoId(String kakaoId);
}


