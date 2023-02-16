package com.dateplanner.admin.place.controller;

import com.dateplanner.admin.place.dto.PlaceCrawlingDto;
import com.dateplanner.admin.place.dto.PlaceStatusDto;
import com.dateplanner.admin.place.service.PlaceAdminService;
import com.dateplanner.admin.place.service.PlaceCrawlingService;
import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/api/v1/admin/places1")
    public PageResult<PlaceStatusDto> getImageUrlNullPlacesV1(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeAdminService.getImageUrlNullPlacesV1(pageable));
    }


    // 어드민 권한 요구 추가 예정
    @Operation(summary = "[GET] 이미지 URL 업데이트를 진행하였으나 이미지가 존재하지 않은 장소 조회",
            description = "이미지 URL 업데이트를 진행하였으나 원본 장소의 이미지가 존재하지 않아 추가를 하지 못했던 장소들을 조회합니다.")
    @GetMapping("/api/v1/admin/places2")
    public PageResult<PlaceStatusDto> getImageUrlNotExistPlacesV1(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeAdminService.getImageUrlNotExistPlacesV1(pageable));
    }


    // 어드민 권한 요구 추가 예정, 테스트용
    @Operation(summary = "[POST] 크롤링 테스트(Selenium)",
            description = "크롤링 테스트")
    @PostMapping("/api/v1/admin/places")
    public PageResult<PlaceCrawlingDto> crawlingPlacesV1(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(placeCrawlingService.searchAndCrawling(pageable));
    }

}
