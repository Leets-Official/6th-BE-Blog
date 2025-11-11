package leets.blogapplication.repository;

import leets.blogapplication.domain.UserSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialRepository extends JpaRepository<UserSocial, Long> {
    Optional<UserSocial> findByProviderAndProviderUserId(String provider, Long providerUserId);
    Optional<UserSocial> findByProviderUserId(Long providerUserId);
}
