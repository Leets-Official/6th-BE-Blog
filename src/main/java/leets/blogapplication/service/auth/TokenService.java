package leets.blogapplication.service.auth;

import leets.blogapplication.config.TokenProvider;
import leets.blogapplication.domain.User;
import leets.blogapplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository accountRepository;

    public TokenService(TokenProvider tokenProvider, RefreshTokenService refreshTokenService, UserRepository accountRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.accountRepository = accountRepository;
    }


    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("access Account not found"));

        return tokenProvider.generateToken(user, Duration.ofHours(2));
        //첫 argument로 들어가는 놈은 무조건 extends UserDetails를 한 놈만 됨
    }

    public String createNewRefreshToken(String email) {
        User user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("refresh Account not found"));
        return tokenProvider.createRefreshToken(user);

    }
}
