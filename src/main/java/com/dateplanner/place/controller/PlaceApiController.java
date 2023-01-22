package com.dateplanner.place.controller;

import com.dateplanner.api.dto.DocumentDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.service.PlaceApiService;
import com.dateplanner.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "PlaceApiController")
@RequiredArgsConstructor
@RestController
public class PlaceApiController {

    /**
     * PlaceService : KAKAO API를 통한 API 호출 및 장소 정보 DB 저장 등 핵심 비즈니스 로직 담당
     * PlaceApiService : 화면 처리를 위한 전용 서비스
     */

    private final PlaceApiService placeApiService;
    private final PlaceService placeService;

    /**
     * 초기 버전은 KAKAO 카테고리 검색 결과를 받아 페이징 / 정렬 처리를 하고 화면 처리를 하지만
     * 추후 추가 기능이 붙어야 한다면 고도화가 필요할 것으로 보인다. (DB에 저장된 장소들 / 카카오 API에서 가져온 정보들 분리해서 출력)
     */
    @Operation(summary = "Place List", description = "[GET] 주소, 카테고리로 장소 리스트 출력")
    @GetMapping("/api/v1/places")
    public Page<DocumentDto> placesV1(String address, String category, @PageableDefault(size = 10, sort = "reviewScore") Pageable pageable) {

        List<DocumentDto> dtos =  placeService.placeSearch(address, category);
        placeService.placePersist(dtos);
        return placeApiService.places(dtos, pageable);
    }

    @Operation(summary = "Place", description = "[GET] 장소 ID로 단일 장소 정보 조회")
    @GetMapping("/api/v1/places/{place_id}")
    public PlaceDto getPlaceV1(@PathVariable("place_id")String placeId) {

        return placeApiService.getPlace(placeId);
    }

}
