package leets.blogapplication.repository;

import leets.blogapplication.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    //추후에 revoked는 false이고, 만료되지 않은 토큰으로 찾아오도록 하는 쿼리 추가
    Optional<RefreshToken> findByUser_Id(Long userId);
}
