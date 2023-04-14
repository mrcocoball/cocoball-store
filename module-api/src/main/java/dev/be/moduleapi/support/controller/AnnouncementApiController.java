package dev.be.moduleapi.support.controller;

import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.support.dto.AnnouncementDto;
import dev.be.moduleapi.support.service.AnnouncementApiService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "8. [고객센터 화면 - 공지사항 - 공지사항 리스트, 상세] AnnouncementApiController - 공지사항 기능 API")
@Timed("business.controller.announcement")
@RequiredArgsConstructor
@RestController
public class AnnouncementApiController {

    private final AnnouncementApiService announcementApiService;
    private final ResponseService responseService;


    @Operation(summary = "[GET] 공지사항 리스트 출력",
            description = "공지사항 리스트를 출력합니다. 장소 상세 정보 조회 시 해당 API도 같이 호출되어야 합니다. <br>" +
                    "검색 조건은 카테고리(ID), 검색 조건(제목, 내용), 키워드입니다.")
    @GetMapping("/api/v1/announcements")
    public PageResult<AnnouncementDto> getAnnouncementsByConditionV1(@Parameter(description = "검색 조건, 기본값 null, title(제목), description(내용)", required = false)
                                                                     @RequestParam(required = false) String condition,
                                                                     @Parameter(description = "카테고리 번호, 기본값 null, 1(일반), 2(업데이트), 3(이벤트), 4(긴급)", required = false)
                                                                     @RequestParam(required = false) Long categoryId,
                                                                     @Parameter(description = "검색 키워드, 기본값 null", required = false)
                                                                     @RequestParam(required = false) String keyword,
                                                                     @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return responseService.getPageResult(announcementApiService.getAnnouncementListWithCondition(condition, categoryId, keyword, pageable));

    }


    @Operation(summary = "[GET] 공지사항 게시글 ID로 단건 조회",
            description = "공지사항 게시글 ID로 게시글을 단건 조회합니다.")
    @GetMapping("/api/v1/announcements/{id}")
    public SingleResult<AnnouncementDto> getAnnouncementsV1(@Parameter(description = "공지사항 ID") @PathVariable("id") Long id) {

        return responseService.getSingleResult(announcementApiService.getAnnouncement(id));

    }
}
