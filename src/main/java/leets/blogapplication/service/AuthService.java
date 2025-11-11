package leets.blogapplication.service;

import leets.blogapplication.domain.RefreshToken;
import leets.blogapplication.domain.User;
import leets.blogapplication.dto.req.LogInWithEmail;
import leets.blogapplication.dto.req.SignUpWithEmail;
import leets.blogapplication.repository.RefreshTokenRepository;
import leets.blogapplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RefreshTokenRepository refreshTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void signUpWithEmail(SignUpWithEmail req){
        User user = User.createUserWithEmail(
                req.getEmail(), req.getName(), req.getBirthDate(),
                req.getNickname(), req.getIntro(), req.getProfileImage(), bCryptPasswordEncoder.encode(req.getPassword()));
        userRepository.save(user);
    }

    public void logInWithEmail(LogInWithEmail req){
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 디버그 로그 (임시)
        System.out.println("PE class = " + passwordEncoder.getClass().getName());
        System.out.println("DB pw prefix = " + user.getPassword().substring(0, Math.min(10, user.getPassword().length())));

        if(!bCryptPasswordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
    }

    public void logoutWithEmail(String ref){
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(ref)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        refreshToken.setRevoked(true);
    }
}

