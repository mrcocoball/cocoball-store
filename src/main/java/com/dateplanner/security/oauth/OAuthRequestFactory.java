package com.dateplanner.security.oauth;

import com.dateplanner.security.oauth.dto.OAuthRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class OAuthRequestFactory {


    /**
     * 추후 네이버, 구글 OAuth 추가 시 추가
     */

    private final KakaoInfo kakaoInfo;


    // provider별 OAuth 요청을 다르게 보냄
    public OAuthRequestDto getRequest(String code, String provider) {

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        if (provider.equals("kakao")) {
            map.add("grant_type", "authorization_code");
            map.add("client_id", kakaoInfo.getKakaoClientId());
            map.add("redirect_uri", kakaoInfo.getBaseUrl() + kakaoInfo.getKakaoRedirectUri());
            map.add("code", code);

            return new OAuthRequestDto(kakaoInfo.getKakaoTokenUrl(), map);
        } else {
            return null;
        }
    }


    // provider별 profileUrl 가져오기
    public String getProfileUrl(String provider) {
        if (provider.equals("kakao")) {
            return kakaoInfo.getKakaoProfileUrl();
        }
        return null;
    }

    @Getter
    @Component
    static class KakaoInfo {
        @Value("${url.base}")
        String baseUrl;

        @Value("${social.kakao.client-id}")
        String kakaoClientId;

        @Value("${social.kakao.redirect}")
        String kakaoRedirectUri;

        @Value("${social.kakao.url.token}")
        private String kakaoTokenUrl;

        @Value("${social.kakao.url.profile}")
        private String kakaoProfileUrl;
    }

}
