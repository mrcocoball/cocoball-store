package com.dateplanner.kakao.service;

import com.dateplanner.advice.exception.CustomRetryException;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j(topic = "SERVICE")
@Service
@RequiredArgsConstructor
public class KakaoCategorySearchService {

    /**
     * requestCategorySearch : 입력받은 위도, 경도, 범위, 카테고리로 KAKAO_카테고리로 장소 검색하기 API 호출
     * recover : 호출 실패 시 처리 메서드
     */

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;


    @Retryable(
            value = {CustomRetryException.class}, // CustomRetryException 발생 시
            maxAttempts = 2, // 초기 요청 포함 2회 재요청
            backoff = @Backoff(delay = 2000) // 딜레이 2초
    )
    public KakaoApiResponseDto requestCategorySearch(double latitude, double longitude, int radius, String category) throws CustomRetryException {

        // TODO : URI Builder 쪽에서 장소 검색하기 API의 1page값만 가지고 오고 있어 수정 필요

        // URI 호출
        URI uri = kakaoUriBuilderService.buildUriForCategorySearch(latitude, longitude, radius, category);
        log.info("[KakaoCategorySearchService requestCategorySearch] URI converting complete, {}", uri);

        // 요청 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        // KAKAO 주소 검색하기 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
    }

    @Recover
    public KakaoApiResponseDto recover(CustomRetryException e) {
        log.error("[KakaoCategorySearchService requestCategorySearch] request failed, {}", e.getMessage());
        return null;
    }


}
