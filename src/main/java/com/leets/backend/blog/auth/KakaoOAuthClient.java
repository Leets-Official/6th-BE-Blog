package com.leets.backend.blog.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class KakaoOAuthClient {

    private static final URI TOKEN_URI = URI.create("https://kauth.kakao.com/oauth/token");
    private static final URI USER_URI = URI.create("https://kapi.kakao.com/v2/user/me");

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public KakaoOAuthClient(RestTemplateBuilder restTemplateBuilder,
                            @Value("${kakao.oauth.client-id}") String clientId,
                            @Value("${kakao.oauth.client-secret:}") String clientSecret,
                            @Value("${kakao.oauth.redirect-uri}") String redirectUri) {
        this.restTemplate = restTemplateBuilder.build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public KakaoOAuthTokenResponse exchangeCodeForToken(String authorizationCode) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("redirect_uri", redirectUri);
        form.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ResponseEntity<KakaoOAuthTokenResponse> response = restTemplate.postForEntity(
                TOKEN_URI,
                entity,
                KakaoOAuthTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("카카오 토큰 발급에 실패했습니다.");
        }
        return response.getBody();
    }

    public KakaoUserProfile fetchUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                USER_URI,
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("카카오 사용자 정보를 불러오지 못했습니다.");
        }

        KakaoUserResponse body = response.getBody();
        String kakaoId = body.getId() != null ? String.valueOf(body.getId()) : null;
        KakaoAccount account = body.getAccount();
        String email = null;
        String nickname = null;
        String name = null;
        String profileImage = null;
        if (account != null) {
            email = account.getEmail();
            KakaoProfile profile = account.getProfile();
            if (profile != null) {
                nickname = profile.getNickname();
                profileImage = profile.getProfileImageUrl();
                name = profile.getNickname();
            }
        }

        if (kakaoId == null) {
            throw new IllegalStateException("카카오 ID를 확인할 수 없습니다.");
        }

        return new KakaoUserProfile(kakaoId, email, nickname, profileImage, name);
    }

    private static class KakaoUserResponse {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("kakao_account")
        private KakaoAccount account;

        public KakaoUserResponse() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public KakaoAccount getAccount() {
            return account;
        }

        public void setAccount(KakaoAccount account) {
            this.account = account;
        }
    }

    private static class KakaoAccount {
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile")
        private KakaoProfile profile;

        public KakaoAccount() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public KakaoProfile getProfile() {
            return profile;
        }

        public void setProfile(KakaoProfile profile) {
            this.profile = profile;
        }
    }

    private static class KakaoProfile {
        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        public KakaoProfile() {
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}