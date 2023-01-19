package com.dateplanner.place.controller;

import com.dateplanner.api.dto.DocumentDto;
import com.dateplanner.api.dto.KakaoApiResponseDto;
import com.dateplanner.api.service.KakaoAddressSearchService;
import com.dateplanner.api.service.KakaoCategorySearchService;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.service.PlaceApiService;
import com.dateplanner.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "PlaceApiController")
@RequiredArgsConstructor
@RestController
public class PlaceApiController {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final PlaceApiService placeApiService;
    private final PlaceService placeService;

    private static final int MAX_LADIUS = 5;

    @Operation(summary = "Place List", description = "[GET] 주소, 카테고리로 장소 리스트 출력")
    @GetMapping("/api/v1/places")
    public List<DocumentDto> placesV1(String address, String category) {

        // TODO 1 : List<DocumentDto> -> Page<PlaceDto> 로 변경 필요
        // TODO 2 : 임시 테스트로 여기에서 API 서비스를 바로 호출하지만 SearchService 구축 후에는 SearchService에서 API 서비스 호출

        List<DocumentDto> addressResults = kakaoAddressSearchService.requestAddressSearch(address).getDocumentList();

        if (Objects.isNull(addressResults) || addressResults.isEmpty()) {
            log.error("[PlaceApiController placesV1] result is null");
            return Collections.emptyList();
        }

        DocumentDto addressDto = addressResults.get(0);

        List<DocumentDto> result = kakaoCategorySearchService.requestCategorySearch(
                Double.valueOf(addressDto.getLatitude()), Double.valueOf(addressDto.getLongitude()), MAX_LADIUS, category
        ).getDocumentList();

        if (Objects.isNull(result) || result.isEmpty()) {
            log.error("[PlaceApiController placesV1] result is null");
            return Collections.emptyList();
        }

        return result;
    }

}
