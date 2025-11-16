package com.leets.backend.blog.service;

import com.leets.backend.blog.config.KakaoProperties;
import com.leets.backend.blog.dto.kakao.KakaoTokenResponseDTO;
import com.leets.backend.blog.dto.kakao.KakaoUserInfoResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoOAuthService(KakaoProperties kakaoProperties, RestTemplate restTemplate) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
    }

    // 인가 코드로 카카오 액세스 토큰 받기
    public KakaoTokenResponseDTO getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        org.springframework.http.HttpEntity<MultiValueMap<String, String>> request =
                new org.springframework.http.HttpEntity<>(params, headers);

        return restTemplate.postForObject(
                kakaoProperties.getTokenUri(),
                request,
                KakaoTokenResponseDTO.class
        );
    }

    // 카카오 액세스 토큰으로 사용자 정보 받기
    public KakaoUserInfoResponseDTO getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        org.springframework.http.HttpEntity<Void> request =
                new org.springframework.http.HttpEntity<>(headers);

        return restTemplate.postForObject(
                kakaoProperties.getUserInfoUri(),
                request,
                KakaoUserInfoResponseDTO.class
        );
    }
}
