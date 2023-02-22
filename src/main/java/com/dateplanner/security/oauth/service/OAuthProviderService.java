package com.dateplanner.security.oauth.service;

import com.dateplanner.advice.exception.OAuthRequestFailedException;
import com.dateplanner.security.oauth.OAuthRequestFactory;
import com.dateplanner.security.oauth.dto.OAuthAccessTokenDto;
import com.dateplanner.security.oauth.dto.OAuthRequestDto;
import com.dateplanner.security.oauth.dto.ProfileDto;
import com.dateplanner.security.oauth.profile.KakaoProfile;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class OAuthProviderService {

    private final OAuthRequestFactory oAuthRequestFactory;
    private final RestTemplate restTemplate;
    private final Gson gson;


    // 전달 받은 인가 코드로 실제 소셜 서비스의 액세스 토큰을 발급받는 메서드
    public OAuthAccessTokenDto getAcessToken(String code, String provider) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        OAuthRequestDto oAuthRequestDto = oAuthRequestFactory.getRequest(code, provider);
        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(oAuthRequestDto.getMap(), httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(oAuthRequestDto.getUrl(), request, String.class);

        // gson을 통해 액세스 토큰을 저장
        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return gson.fromJson(response.getBody(), OAuthAccessTokenDto.class);
            }

        } catch (Exception e) {
            throw new OAuthRequestFailedException();
        }

        throw new OAuthRequestFailedException();
    }

    // 액세스 토큰을 활용하여 이메일 정보를 받아오는 메서드
    public ProfileDto getProfile(String accessToken, String provider) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        String profileUrl = oAuthRequestFactory.getProfileUrl(provider);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(profileUrl, request, String.class);

        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return extractProfile(response, provider);
            }

        } catch (Exception e) {
            throw new OAuthRequestFailedException();
        }

        throw new OAuthRequestFailedException();

    }


    // response에서 프로필 Dto를 추출
    private ProfileDto extractProfile(ResponseEntity<String> response, String provider) {

        if(provider.equals("kakao")) {
            KakaoProfile kakaoProfile = gson.fromJson(response.getBody(), KakaoProfile.class);
            return new ProfileDto(kakaoProfile.getKakao_account().getEmail(), kakaoProfile.getProperties().getNickname());
        }

        return null;
    }

}
