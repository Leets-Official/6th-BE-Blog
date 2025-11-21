package com.leets.backend.blog.service;

import com.leets.backend.blog.config.CustomUserDetails;
import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.AuthResponse;
import com.leets.backend.blog.dto.UserLoginRequest;
import com.leets.backend.blog.dto.UserSignUpRequest;
import com.leets.backend.blog.entity.RefreshToken;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.DuplicateEmailException;
import com.leets.backend.blog.exception.DuplicateNicknameException;
import com.leets.backend.blog.exception.TokenRefreshException;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public User signUp(UserSignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicateNicknameException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User(
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                "EMAIL"
        );

        return userRepository.save(newUser);
    }

    @Transactional
    public AuthResponse login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue) {
        return refreshTokenService.findByToken(refreshTokenValue)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), null, Collections.emptyList());
                    String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());
                    return new AuthResponse(newAccessToken, newRefreshToken.getToken());
                })
                .orElseThrow(() -> new TokenRefreshException(refreshTokenValue, "Refresh token not found"));
    }
}
