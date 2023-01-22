package com.dateplanner.place.service;

import com.dateplanner.api.dto.DocumentDto;
import com.dateplanner.api.service.KakaoAddressSearchService;
import com.dateplanner.api.service.KakaoCategorySearchService;
import com.dateplanner.place.entity.Category;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final PlaceApiService placeApiService;
    private static final int MAX_LADIUS = 5;

    public List<DocumentDto> placeSearch(String address, String category) {

        /**
         * KAKAO 주소 검색하기 API 호출하여 주소 변환
         */

        List<DocumentDto> addressResults = kakaoAddressSearchService.requestAddressSearch(address).getDocumentList();

        if (Objects.isNull(addressResults) || addressResults.isEmpty()) {
            log.error("[PlaceService placeSearch] address search result is null");
            return Collections.emptyList();
        }

        /**
         * 변환된 주소 + 카테고리 + 설정 범위로 KAKAO 카테고리로 장소 검색하기 API 호출하여 장소 리스트 전달 받기
         */

        DocumentDto addressDto = addressResults.get(0);

        List<DocumentDto> results = kakaoCategorySearchService.requestCategorySearch(
                Double.valueOf(addressDto.getLatitude()), Double.valueOf(addressDto.getLongitude()), MAX_LADIUS, category
        ).getDocumentList();

        if (Objects.isNull(results) || results.isEmpty()) {
            log.error("[PlaceService placeSearch] category search result is null");
            return Collections.emptyList();
        }

        return results;
    }

    public Map<String, Long> placePersist(List<DocumentDto> results) {

        // 결과 HashMap
        Map<String, Long> resultMap = new HashMap<>();

        if (Objects.isNull(results) || results.isEmpty()) {
            log.error("[PlaceService placePersist] category search result is null");
            resultMap.put("number of saved places : ", null);
            return resultMap;
        }

        /**
         * 전달 받은 장소 리스트 DB 내 중복 여부 체크 후 DB에 저장
         */

        log.info("[PlaceService placeSearchAndSave] DocumentDto -> Repository save start");
        int convertResultCount = 0;
        int nestedResultCount = 0;

        for (DocumentDto result : results) {

            result.splitAddress(result.getAddressName());
            if (placeRepository.existsByPlaceId(result.getPlaceId())) {
                nestedResultCount += 1;
            }

            if (!placeRepository.existsByPlaceId(result.getPlaceId())) {
                placeRepository.save(Place.of(
                        Category.of(result.getCategoryGroupId()),
                        result.getPlaceName(),
                        result.getPlaceId(),
                        result.getPlaceUrl(),
                        result.getAddressName(),
                        result.getRegion1DepthName(),
                        result.getRegion2DepthName(),
                        result.getRegion3DepthName(),
                        result.getLongitude(),
                        result.getLatitude(),
                        0L,
                        0L));
                convertResultCount += 1;
            }
        }

        resultMap.put("number of saved places : ", Long.valueOf(convertResultCount));
        resultMap.put("number of nested places : ", Long.valueOf(nestedResultCount));
        resultMap.put("total number of searched places : ", Long.valueOf(results.size()));
        log.info("persist complete, {}", resultMap);
        return resultMap;
    }
}
