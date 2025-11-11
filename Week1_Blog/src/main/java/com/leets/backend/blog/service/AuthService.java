package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.request.LoginRequestDTO;
import com.leets.backend.blog.dto.request.UserCreateRequestDTO;
import com.leets.backend.blog.dto.response.LoginResponseDTO;
import com.leets.backend.blog.dto.response.TokenResponseDTO;
import com.leets.backend.blog.dto.response.UserResponseDTO;
import com.leets.backend.blog.entity.*;
import com.leets.backend.blog.exception.CustomException;
import com.leets.backend.blog.exception.ErrorCode;
import com.leets.backend.blog.repository.*;
import com.leets.backend.blog.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO userLogin(LoginRequestDTO request) {
        LoginResponseDTO response;

        if (request.getKakaoLogin()){
            //response = loginWithKakao();  카카오 로그인
            return null;
        }
        else{
            response = loginWithEmail(request); // 이메일 로그인
        }

        return response;
    }

    @Transactional
    public LoginResponseDTO loginWithEmail(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null)
            throw new CustomException(ErrorCode.INVALID_EMAIL);

        try {
            // 비밀번호 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // 비밀번호 불일치
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // DB에 RefreshToken 저장 (기존 토큰 삭제)
        refreshTokenRepository.deleteByUser(user);
        RefreshToken token = new RefreshToken(refreshToken, user, Instant.now().plus(7, ChronoUnit.DAYS));
        refreshTokenRepository.save(token);

        return new LoginResponseDTO(user.getNickname());
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        RefreshToken dbToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (dbToken.getExpiryDate().isBefore(Instant.now())) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return jwtTokenProvider.createAccessToken(username);
    }

    @Transactional
    public void logout(String refreshToken) {
        // DB에 있는 RefreshToken 삭제
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }



    public UserResponseDTO createUser(UserCreateRequestDTO dto){
        if (dto.getIsKakaoLogin()){
            if (userRepository.existsUserByKakaoId(dto.getKakaoId()))
                throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ID);
        }
        else {
            if (userRepository.existsUserByEmail(dto.getEmail()))
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        if (userRepository.existsUserByNickname(dto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        User newUser = User.createUser(dto, passwordEncoder.encode(dto.getPassword()));

        return UserResponseDTO.toDTO(userRepository.save(newUser));
    }
}
