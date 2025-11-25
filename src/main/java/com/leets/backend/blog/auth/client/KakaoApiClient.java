package com.leets.backend.blog.auth.client;

import com.leets.backend.blog.auth.controller.dto.response.KakaoUserInfo;
import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String clientId;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;

    public KakaoApiClient(@Value("${oauth.kakao.client-id}") String clientId,
                          @Value("${oauth.kakao.redirect-uri}") String redirectUri,
                          @Value("${oauth.kakao.token-uri}") String tokenUri,
                          @Value("${oauth.kakao.user-info-uri}") String userInfoUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }

    // 인가 코드를 사용하여 카카오로부터 액세스 토큰을 발급받습니다.
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK &&
                Objects.requireNonNull(response.getBody()).containsKey("access_token")) {

                return (String) response.getBody().get("access_token");
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.KAKAO_TOKEN_REQUEST_FAILED, "카카오 토큰 발급에 실패했습니다.");
        }
        throw new CustomException(ErrorCode.KAKAO_RESPONSE_NULL, "카카오 서버로부터 응답이 없습니다.");
    }

    // 카카오 액세스 토큰을 사용하여 사용자 정보를 조회합니다.
    public KakaoUserInfo getUserInfo(String KakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + KakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            // 사용자 정보 조회 요청
            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUri,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("Kakao_Account");

                if (kakaoAccount == null || !(Boolean) kakaoAccount.getOrDefault("is_email_valid", false)) {
                    throw new CustomException(ErrorCode.KAKAO_EMAIL_SCOPE_REQUIRED, "카카오 이메일 정보 접근 동의가 필요합니다.");
                }

                String email = (String) kakaoAccount.get("email");

                Map<String, Object> profile =  (Map<String, Object>) kakaoAccount.get("profile");
                String nickname = (String) profile.get("nickname");
                String profileImageUrl = (String) profile.get("profile_image_url");

                return new KakaoUserInfo(email, nickname, profileImageUrl);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.KAKAO_USERINFO_REQUEST_FAILED, "카카오 사용자 정보 요청에 실패했습니다.");
        }
        throw new CustomException(ErrorCode.KAKAO_RESPONSE_NULL, "카카오 서버로부터 응답이 없습니다.");
    }
}
