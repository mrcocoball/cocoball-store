package dev.be.moduleapi.security.oauth.service;

import com.google.gson.Gson;
import dev.be.moduleapi.advice.exception.OAuthRequestFailedException;
import dev.be.moduleapi.security.oauth.OAuthRequestFactory;
import dev.be.moduleapi.security.oauth.dto.OAuthAccessTokenDto;
import dev.be.moduleapi.security.oauth.dto.OAuthRequestDto;
import dev.be.moduleapi.security.oauth.dto.ProfileDto;
import dev.be.moduleapi.security.oauth.profile.KakaoProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
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
    private final Environment env;
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

    // 카카오 언링크 요청
    public void kakaoUnlink(String accessToken) {

        String unlinkUrl = env.getProperty("social.kakao.url.unlink");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("[OAuthProviderService kakaoUnlink] unlink complete");
            return;
        }

        throw new OAuthRequestFailedException();

    }

}
