package com.dateplanner.place.service;

import com.dateplanner.advice.exception.SearchResultNotFoundException;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.kakao.dto.MetaDto;
import com.dateplanner.kakao.service.KakaoAddressSearchService;
import com.dateplanner.kakao.service.KakaoCategorySearchService;
import com.dateplanner.place.entity.Category;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private static final int MAX_LADIUS = 5; // km


    /**
     * KAKAO 주소 검색하기 API 호출하여 주소 및 좌표 변환 -> 카테고리 검색 및 DB 조회에 활용
     */

    public DocumentDto getPlaceLongitudeAndLatitude(String address) {

        List<DocumentDto> addressResults = kakaoAddressSearchService.requestAddressSearch(address).getDocumentList();

        if (Objects.isNull(addressResults) || addressResults.isEmpty()) {
            log.error("[PlaceService getPlaceLongitudeAndLatitude] address search result is null");
            throw new SearchResultNotFoundException();
        }

        return addressResults.get(0);

    }

    /**
     * 변환된 주소 + 카테고리 + 설정 범위로 KAKAO 카테고리로 장소 검색하기 API 호출하여 장소 리스트 전달 받기
     */

    public KakaoApiResponseDto placeSearchByKakao(DocumentDto addressDto, String category) {

        // TODO : URI Builder 쪽에서 장소 검색하기 API의 1page값만 가지고 오고 있어 수정 필요

        return kakaoCategorySearchService.requestCategorySearch(
                Double.valueOf(addressDto.getLatitude()), Double.valueOf(addressDto.getLongitude()), MAX_LADIUS, category);
    }

    /**
     * 카테고리 장소 검색 결과를 DB에 저장하기
     */

    public List<String> placePersist(KakaoApiResponseDto dto) {

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        List<DocumentDto> results = dto.getDocumentList();

        // 결과 HashMap
        Map<String, Long> resultMap = new HashMap<>();

        if (Objects.isNull(results) || results.isEmpty()) {
            log.error("[PlaceService placePersistV2] category search result is null");
            resultMap.put("number of saved places : ", null);
            throw new SearchResultNotFoundException();
        }

        // 전달 받은 장소 리스트 DB 내 중복 여부 체크 후 DB에 저장
        log.info("[PlaceService placePersistV2] DocumentDto -> Repository save start");
        int convertResultCount = 0;
        int nestedResultCount = 0;

        List<String> region2List = new ArrayList<>();

        log.info("[PlaceService placePersistV2] get region2DepthName list....");
        for (DocumentDto result : results) {
            result.splitAddress(result.getAddressName());
            String region2DepthName = result.getRegion2DepthName();
            if (!region2List.contains(region2DepthName)) {
                region2List.add(region2DepthName);
            }
        }

        log.info("[PlaceService placePersistV2] get region2DepthName list complete, {}", region2List);

        String region1DepthName = results.get(0).getRegion1DepthName();

        log.info("[PlaceService placePersistV2] get placeId list");

        List<String> placeIds = placeRepository.findPlaceIdByRegion1DepthNameAndRegion2DepthName(region1DepthName, region2List);

        log.info("[PlaceService placePersistV2] duplication check and persist");

        for (DocumentDto result : results) {

            result.splitAddress(result.getAddressName());

            if (placeIds.contains(result.getPlaceId())) {
                nestedResultCount += 1;
            }

            if (!placeIds.contains(result.getPlaceId())) {
                placeRepository.save(Place.of(
                        Category.of(result.getCategoryGroupId()),
                        result.getPlaceName(),
                        result.getPlaceId(),
                        result.getPlaceUrl(),
                        result.getAddressName(),
                        result.getRoadAddressName(),
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

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        resultMap.put("number of saved places : ", Long.valueOf(convertResultCount));
        resultMap.put("number of nested places : ", Long.valueOf(nestedResultCount));
        resultMap.put("total number of searched places : ", Long.valueOf(results.size()));
        log.info("persist complete, {}", resultMap);
        return region2List;
    }


    /**
     * API 호출 결과 비교용 메서드
     */
    public MetaDto getMetaDto(String address, String category) {
        DocumentDto addressDto = getPlaceLongitudeAndLatitude(address);
        return placeSearchByKakao(addressDto, category).getMetaDto();
    }

    public List<DocumentDto> getDocumentDto(String address, String category) {
        DocumentDto addressDto = getPlaceLongitudeAndLatitude(address);
        return placeSearchByKakao(addressDto, category).getDocumentList();
    }
}
