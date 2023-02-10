package com.dateplanner.place.controller;

import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.place.dto.PlaceRecommendationDto;
import com.dateplanner.place.service.PlaceRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "3. [장소 추천 화면] PlaceRecommendationApiController - 장소 추천 API")
@RequiredArgsConstructor
@RestController
public class PlaceRecommendationApiController {

    private final PlaceRecommendationService placeRecommendationService;
    private final ResponseService responseService;


    // TODO : 현재는 카테고리 무관하게 평점 순 상위 50개 장소를 추천해주고 있으나, 카테고리 별로 가져와야 할 경우 10개씩 5번 쿼리를 날려야 할 것으로 보임
    //  아니면 넉넉하게 한 번에 카테고리 무관하게 100개 정도 가져오고 애플리케이션 내에서 카테고리별로 모아서 뿌리는 방법도 있으나 카테고리 별로 10개가 무조건 나온다는 보장은 없어 고민이 필요

    @Operation(summary = "[GET] 선택된 주소 정보 (시/도, 군/구, 동/읍/면) 기반 카테고리 무관 평점 순 상위 50개 장소 추천",
            description = "주소 정보를 시/도, 군/구, 동/읍/면 까지 입력하여 검색할 경우 카테고리와 상관없이 평점 순으로 상위 50개의 장소를 추천합니다. <br>" +
                    "각 주소 정보는 공란이 되더라도 검색은 가능하지만 주소 정보가 생략이 되기 때문에 검색 폭이 넓어질 수 있습니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 이들 장소에 대한 상세 정보는 장소 단건 조회 API /api/v1/places/{place_id} 를 활용합니다.")
    @GetMapping("/api/v1/recommendation")
    public PageResult<PlaceRecommendationDto> getRecommendationV1(@Parameter(description = "시/도") @RequestParam(required = false) String region1,
                                                                  @Parameter(description = "군/구") @RequestParam(required = false) String region2,
                                                                  @Parameter(description = "동/읍/면") @RequestParam(required = false) String region3,
                                                                  @PageableDefault(size = 10, sort = "avgReviewScore") Pageable pageable) {

        return responseService.getPageResult(placeRecommendationService.getPlaceRecommendation(region1, region2, region3, pageable));
    }

}
