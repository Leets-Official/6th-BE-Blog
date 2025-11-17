package com.leets.backend.blog.service;

import com.leets.backend.blog.config.KakaoOAuthProperties;
import com.leets.backend.blog.dto.kakao.KakaoTokenResponse;
import com.leets.backend.blog.dto.kakao.KakaoUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthClient {

    private final KakaoOAuthProperties props;

    @Autowired
    private final RestTemplate restTemplate;

    public KakaoOAuthClient(KakaoOAuthProperties props, RestTemplate restTemplate) {
        this.props = props;
        this.restTemplate = restTemplate; //주입받은 RestTemplate 사용
    }

    /** 인가코드로 카카오 access_token 요청 */
    public KakaoTokenResponse getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", props.getClientId());
        body.add("redirect_uri", props.getRedirectUri());
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                props.getTokenUri(),
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        return response.getBody();
    }

    /** 카카오 access_token으로 유저 정보 조회 */
    public KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                props.getUserInfoUri(),
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        return response.getBody();
    }

    /** 카카오 로그인 화면으로 리다이렉트할 URL 생성 */
    public String buildAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri();
    }
}
