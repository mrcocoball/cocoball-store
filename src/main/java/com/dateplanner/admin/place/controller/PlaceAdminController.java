package com.dateplanner.admin.place.controller;

import com.dateplanner.admin.place.dto.PlaceCrawlingDto;
import com.dateplanner.admin.place.dto.PlaceStatusDto;
import com.dateplanner.admin.place.service.PlaceAdminService;
import com.dateplanner.admin.place.service.PlaceCrawlingService;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "8. [어드민 전용] PlaceAdminController - 장소 정보 수정 관련 API")
@RequiredArgsConstructor
@RestController // 테스트 이후 Controller로 변환하여 SSR 페이지에 사용할 예정
public class PlaceAdminController {

    private final PlaceAdminService placeAdminService;
    private final PlaceCrawlingService placeCrawlingService;
    private final ResponseService responseService;


    // 어드민 권한 요구 추가 예정
    @Operation(summary = "[GET] 이미지 URL이 추가가 되지 않은 장소 조회",
            description = "DB에 생성이 되었으나 이미지 URL이 추가 되지 않은 장소(이미지 URL이 NULL인 장소)들을 조회합니다.")
    @GetMapping("/api/v1/admin/nullPlaces")
    public PageResult<PlaceStatusDto> getImageUrlNullPlacesV1(@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeAdminService.getImageUrlNullPlacesV1(pageable));
    }


    // 어드민 권한 요구 추가 예정
    @Operation(summary = "[GET] 이미지 URL 업데이트를 진행하였으나 이미지가 존재하지 않은 장소 조회",
            description = "이미지 URL 업데이트를 진행하였으나 원본 장소의 이미지가 존재하지 않아 추가를 하지 못했던 장소들을 조회합니다.")
    @GetMapping("/api/v1/admin/notExistPlaces")
    public PageResult<PlaceStatusDto> getImageUrlNotExistPlacesV1(@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeAdminService.getImageUrlNotExistPlacesV1(pageable));
    }


    // 어드민 권한 요구 추가 예정, 테스트용
    @Operation(summary = "[POST] 장소 이미지, 설명 크롤링 요청 및 결과 확인 테스트",
            description = "이미지 URL이 추가되지 않은 장소들을 조회한 뒤 해당 장소들에 대해 크롤링을 요청하고 크롤링 결과를 조회합니다. <br>" +
                    "실제 DB 장소에는 저장되지는 않습니다.")
    @PostMapping("/api/v1/admin/crawling")
    public PageResult<PlaceCrawlingDto> crawlingPlacesV1(@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeCrawlingService.searchAndCrawling(pageable));
    }

    // 어드민 권한 요구 추가 예정, 테스트용
    @Operation(summary = "[POST] 장소 이미지, 설명 크롤링 요청 및 결과 확인",
            description = "이미지 URL이 추가되지 않은 장소들을 조회한 뒤 해당 장소들에 대해 크롤링을 요청하고 <br>" +
                    "크롤링된 정보를 토대로 장소 정보를 업데이트합니다. 업데이트된 장소의 개수가 반환됩니다.")
    @PostMapping("/api/v1/admin/crawlingAndUpdate")
    public SingleResult<Long> crawlingAndUpdatePlacesV1() {

        List<PlaceCrawlingDto> dtos = placeCrawlingService.searchAndCrawling();

        return responseService.getSingleResult(placeAdminService.updatePlacesV1(dtos));
    }

}
