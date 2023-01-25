package com.dateplanner.place.controller;

import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.kakao.dto.MetaDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.service.PlaceApiService;
import com.dateplanner.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "PlaceApiController")
@RequiredArgsConstructor
@RestController
public class PlaceApiController {

    /**
     * PlaceService : KAKAO API를 통한 API 호출 및 장소 정보 DB 저장 등 핵심 비즈니스 로직 담당
     * PlaceApiService : 화면 처리를 위한 전용 서비스
     * ResponseService : 응답 처리를 위한 컨트롤러 공통 서비스
     */

    private final PlaceApiService placeApiService;
    private final PlaceService placeService;
    private final ResponseService responseService;


    @Operation(summary = "장소 검색", description = "[GET] 주소, 카테고리로 장소 리스트 출력")
    @GetMapping("/api/v1/places")
    public PageResult<PlaceDto> placesV1(@Parameter(description = "입력 주소") String address,
                                         @Parameter(description = "카테고리 코드") String category,
                                         @PageableDefault(size = 10, sort = "reviewScore") Pageable pageable) {

        DocumentDto addressDto = placeService.getPlaceLongitudeAndLatitude(address);
        KakaoApiResponseDto dto =  placeService.placeSearchByKakao(addressDto, category);
        placeService.placePersist(dto);
        return responseService.getPageResult(placeApiService.getPlaces(addressDto, dto, pageable));
    }

    @Operation(summary = "장소 단건 조회", description = "[GET] 장소 ID로 단일 장소 정보 조회")
    @GetMapping("/api/v1/places/{place_id}")
    public SingleResult<PlaceDto> getPlaceV1(@Parameter(description = "장소 ID") @PathVariable("place_id")String placeId) {

        return responseService.getSingleResult(placeApiService.getPlace(placeId));
    }


    /**
     * API 테스트 비교용 메서드
     */
    @Operation(summary = "장소 검색 (KAKAO)", description = "[GET] 주소, 카테고리로 장소 리스트 출력 (KAKAO)")
    @GetMapping("/api/v1/placesKakao")
    public PageResult<DocumentDto> placesKakaoV1(@Parameter(description = "입력 주소") String address,
                                                 @Parameter(description = "카테고리 코드") String category,
                                                 @PageableDefault(size = 10, sort = "reviewScore") Pageable pageable) {

        DocumentDto addressDto = placeService.getPlaceLongitudeAndLatitude(address);
        KakaoApiResponseDto dto =  placeService.placeSearchByKakao(addressDto, category);
        placeService.placePersist(dto);
        return responseService.getPageResult(placeApiService.getPlacesByKakao(dto, pageable));
    }

    @Operation(summary = "KAKAO API 테스트 (Meta)", description = "[GET] 카카오 API Meta Dto 테스트용")
    @GetMapping("/api/v1/test/meta")
    public MetaDto testMetaV1(String address, String category) {

        return placeService.getMetaDto(address, category);
    }

    @Operation(summary = "KAKAO API 테스트 (Document)", description = "[GET] 카카오 API Document Dto 테스트용")
    @GetMapping("/api/v1/test/document")
    public List<DocumentDto> testDocumentV1(String address, String category) {

        return placeService.getDocumentDto(address, category);
    }

}
