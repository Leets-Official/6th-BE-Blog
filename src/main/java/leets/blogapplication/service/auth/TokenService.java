package leets.blogapplication.service.auth;

import leets.blogapplication.config.TokenProvider;
import leets.blogapplication.domain.RefreshToken;
import leets.blogapplication.domain.User;
import leets.blogapplication.repository.RefreshTokenRepository;
import leets.blogapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(TokenProvider tokenProvider, RefreshTokenService refreshTokenService,
                        UserRepository accountRepository, RefreshTokenRepository refreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.accountRepository = accountRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("access Account not found"));

        return tokenProvider.generateAccessToken(user.getEmail(), userId, Duration.ofHours(2));
        //첫 argument로 들어가는 놈은 무조건 extends UserDetails를 한 놈만 됨
    }

    public String createNewRefreshToken(String email) {
        User user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("refresh Account not found"));
        String ref = tokenProvider.generateRefreshToken(email, user.getId(), Duration.ofDays(1));
        refreshTokenRepository.save(RefreshToken.createRefreshToken(ref, user));
        return ref;
    }

    public User getUserFromAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = auth.getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);
        User user = accountRepository.getById(userId);
        return user;
    }
}
