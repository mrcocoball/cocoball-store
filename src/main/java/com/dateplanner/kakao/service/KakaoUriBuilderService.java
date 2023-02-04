package com.dateplanner.kakao.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "SERVICE")
@Service
public class KakaoUriBuilderService {

    /**
     * LOCAL_SEARCH_ADDRESS_URL : KAKAO_주소 검색하기 API 호출 URL
     * LOCAL_CATEGORY_SEARCH_URL : KAKAO_카테고리로 장소 검색하기 API 호출 URL
     * buildUriForAddressSearch : KAKAO_주소 검색하기 API를 위한 URI Builder
     * buildUriForCategorySearch : KAKAO_카테고리로 장소 검색하기 API를 위한 URI Builder
     */

    private static final String LOCAL_ADDRESS_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String LOCAL_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";


    public URI buildUriForAddressSearch(String address) {

        /**
         * 주소 address 기반
         */

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(LOCAL_ADDRESS_SEARCH_URL);
        uriBuilder.queryParam("query", address);

        URI uri = uriBuilder.build().encode().toUri();
        log.info("[KakaoUriBuilderService buildUriForAddressSearch] address {}, uri: {}", address, uri);

        return uri;

    }

    public URI buildUriForCategorySearch(double latitude, double longitude, int radius, String category, int page) {

        /**
         * 입력 위도, 경도, 반경거리 기반
         * radius = 상수 처리
         * category = 사용자 선택
         * buildUriForAddressSearch를 통해 입력된 주소의 위도, 경도를 받고 category, radius를 포함하여 다시 전달
         */

        int meterRadius = radius * 1000;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(LOCAL_CATEGORY_SEARCH_URL);
        uriBuilder.queryParam("category_group_code", category);
        uriBuilder.queryParam("y", latitude);
        uriBuilder.queryParam("x", longitude);
        uriBuilder.queryParam("radius", meterRadius);
        uriBuilder.queryParam("sort", "distance");
        uriBuilder.queryParam("page", page);

        URI uri = uriBuilder.build().encode().toUri(); // UTF-8로 인코딩해줌
        log.info("[KakaoUriBuilderService buildUriForCategorySearch] uri: {}", uri);

        return uri;

    }


}
