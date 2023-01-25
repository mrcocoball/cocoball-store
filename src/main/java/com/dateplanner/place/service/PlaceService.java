package com.dateplanner.place.service;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
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
            return null;
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

    public Map<String, Long> placePersist(KakaoApiResponseDto dto) {

        List<DocumentDto> results = dto.getDocumentList();

        // 결과 HashMap
        Map<String, Long> resultMap = new HashMap<>();

        if (Objects.isNull(results) || results.isEmpty()) {
            log.error("[PlaceService placePersist] category search result is null");
            resultMap.put("number of saved places : ", null);
            return resultMap;
        }

        // 전달 받은 장소 리스트 DB 내 중복 여부 체크 후 DB에 저장
        // TODO: 현재 중복 여부 체크를 할 때마다 반복문으로 select 쿼리가 나가고 있어 성능 이슈 우려 있음, 추후 리팩토링 필요
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

        resultMap.put("number of saved places : ", Long.valueOf(convertResultCount));
        resultMap.put("number of nested places : ", Long.valueOf(nestedResultCount));
        resultMap.put("total number of searched places : ", Long.valueOf(results.size()));
        log.info("persist complete, {}", resultMap);
        return resultMap;
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
