package com.dateplanner.place.controller;

import com.dateplanner.admin.user.entity.User;
import com.dateplanner.advice.exception.CategoryInvalidException;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.dto.PlaceDetailDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.service.PlaceApiService;
import com.dateplanner.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "1. [장소 검색 화면, 장소 상세 정보] PlaceApiController - 장소 검색 API")
@RequiredArgsConstructor
@RestController
public class PlaceApiController {


    private final PlaceApiService placeApiService;
    private final PlaceService placeService;
    private final ResponseService responseService;
    private static final String[] CATEGORIES = {"AT4", "CE7", "CT1", "FD6", "SW8"};


    @Operation(summary = "[GET] 주소, 카테고리로 장소 리스트 출력",
            description = "주소 (String) 과 카테고리 List<String>을 입력하여 요청하면 <br>" +
                    "입력 주소가 좌표로 변환되고 해당 좌표 기준으로 장소 리스트를 출력합니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용, 특정 장소를 조회하거나 북마크/리뷰/플랜 등 장소 ID(place_id) 가 필요한 곳에서는 place_id (String)을 사용합니다.")
    @GetMapping("/api/v1/places")
    public PageResult<PlaceDto> getPlacesV1(@Parameter(description = "입력 주소") String address,
                                            @Parameter(description = "카테고리 코드, AT4 관광명소, CE7 카페, " + "CT1 문화시설, FD6 음식점, SW8 지하철")
                                            @RequestParam(value = "categories", required = false, defaultValue = "") List<String> categories,
                                            @ParameterObject @PageableDefault(size = 10, sort = "avgReviewScore") Pageable pageable) {

        // TODO : Stream() 활용해서 코드 가독성 깔끔하게 처리해볼 것
        for (String category : categories) {
            if (!Arrays.stream(CATEGORIES).anyMatch(category::equals)) {
                throw new CategoryInvalidException();
            }
        }

        DocumentDto addressDto = placeService.getPlaceLongitudeAndLatitude(address);
        KakaoApiResponseDto dto = placeService.placeSearchByKakao(addressDto, categories);
        List<String> region2List = placeService.placePersist(dto);
        return responseService.getPageResult(placeApiService.getPlaces(addressDto, dto, region2List, categories, pageable));
    }

    @Operation(summary = "[GET] 장소 ID로 단일 장소 정보 조회",
            description = "장소 ID (place_id)를 통해 특정 장소의 정보를 가져옵니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 해당 API 및 데이터는 세부 플랜 장소 검색 시에도 활용합니다.")
    @GetMapping("/api/v1/places/{place_id}")
    public SingleResult<PlaceDetailDto> getPlaceV1(@Parameter(description = "장소 ID, 장소 리스트의 place_id를 사용합니다")
                                                   @PathVariable("place_id") String placeId,
                                                   Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        return responseService.getSingleResult(placeApiService.getPlace(placeId, nickname));
    }

}
