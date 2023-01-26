package com.dateplanner.bookmark.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.bookmark.dto.BookmarkDto;
import com.dateplanner.bookmark.service.BookmarkApiService;
import com.dateplanner.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "BookmarkApiController - 북마크 기능 API")
@RequiredArgsConstructor
@RestController
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final BookmarkApiService bookmarkApiService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "북마크 저장, 사용자 로그인이 되어야 함", description = "[POST] 북마크 저장")
    @PostMapping("/api/v1/bookmarks")
    public SingleResult<Long> saveBookmarkV1(
            @Parameter(description = "사용자 정보, uid에 id만 넣어주세요", required = true) Principal principal,
            @Parameter(description = "장소 ID, 프론트엔드에서 장소 ID 넣어줄 것", required = true) @RequestParam String placeId) {
        String uid = principal.getName();
        log.info("authentication, uid : {}", uid);

        return responseService.getSingleResult(bookmarkService.saveBookmark(uid, placeId));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "북마크 조회, 사용자 로그인이 되어야 함", description = "[GET] 북마크 조회")
    @GetMapping("/api/v1/bookmarks")
    public PageResult<BookmarkDto> getBookmarksV1(
            @Parameter(description = "사용자 정보, uid에 id만 넣어주세요", required = true) Principal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        String uid = principal.getName();
        log.info("authentication, uid : {}", uid);

        return responseService.getPageResult(bookmarkApiService.getBookmarkList(uid, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "북마크 삭제, Dto의 uid와 사용자 인증 정보의 uid 일치 여부 확인 필요", description = "[DELETE] 북마크 삭제")
    @DeleteMapping("/api/v1/bookmarks/{id}")
    public CommonResult deleteBookmarkV1(
            @Parameter(description = "북마크 ID, 프론트엔드에서 북마크 ID 넣어줄 것", required = true) @PathVariable("id") Long id) {

        /**
         * 프론트엔드에서 삭제 버튼을 인증 상태에 따라 표시하고
         * 그 과정에서 북마크 Dto의 uid와 현재 사용자 인증 정보의 uid 비교 가능하다면 그대로 진행해도 되나
         * 그러지 않을 경우엔 principal을 파라미터로 받고
         * 사용자 정보 uid를 토대로 DB에서 삭제하려는 북마크의 uid와 비교를 한 후 삭제 처리를 해야할 것으로 보임
         */

        bookmarkService.deleteBookmark(id);

        return responseService.getSuccessResult();
    }
}
