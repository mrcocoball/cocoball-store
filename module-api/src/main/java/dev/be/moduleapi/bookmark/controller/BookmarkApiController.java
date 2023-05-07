package dev.be.moduleapi.bookmark.controller;

import dev.be.moduleapi.api.model.CommonResult;
import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.bookmark.dto.BookmarkDto;
import dev.be.moduleapi.bookmark.service.BookmarkApiService;
import dev.be.moduleapi.bookmark.service.BookmarkService;
import dev.be.modulecore.domain.user.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "4. [마이 페이지 - 북마크 목록] BookmarkApiController - 북마크 기능 API")
@Timed("business.controller.bookmark")
@RequiredArgsConstructor
@RestController
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final BookmarkApiService bookmarkApiService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 북마크 목록에 저장, 사용자 로그인 되어 있어야 함",
            description = "지정한 장소를 북마크 목록에 저장합니다. 저장하려는 장소의 place_id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[POST] /api/v1/bookmarks?placeId={place_id} <br><br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 북마크를 저장하려는 유저를 체크합니다.")
    @PostMapping("/api/v1/bookmarks")
    public SingleResult<BookmarkDto> saveBookmarkV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @Parameter(description = "장소 정보의 place_id (String)", required = true) @RequestParam String placeId) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        return responseService.getSingleResult(bookmarkService.saveBookmark(nickname, placeId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 북마크 목록 출력, 사용자 로그인 되어 있어야 함",
            description = "요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 해당 유저가 저장해둔 북마크 리스트를 출력합니다. <br>" +
                    "이들 장소에 대한 상세 정보는 장소 단건 조회 API /api/v1/places/{place_id} 를 활용합니다.")
    @GetMapping("/api/v1/bookmarks")
    public PageResult<BookmarkDto> getBookmarksV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        String email = user.getEmail();

        return responseService.getPageResult(bookmarkApiService.getBookmarkList(email, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 북마크 삭제, 사용자 로그인 되어 있어야 함",
            description = "지정한 북마크를 삭제합니다. 삭제하려는 북마크의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/bookmarks/{id} <br><br>" +
                    "사용자가 북마크의 id를 알 가능성은 낮으나, 만약 북마크 id를 알아서 다른 북마크를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/bookmarks/{id}")
    public CommonResult deleteBookmarkV1(
            @Parameter(description = "북마크 정보의 id (Long)", required = true) @PathVariable("id") Long id) {

        bookmarkService.deleteBookmark(id);

        return responseService.getSuccessResult();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 북마크 삭제(장소 ID 활용), 사용자 로그인 되어 있어야 함",
            description = "지정한 북마크를 삭제합니다. 삭제하려는 북마크의 장소 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/bookmarks/{place_id} <br><br>" +
                    "사용자가 북마크의 id를 알 가능성은 낮으나, 만약 북마크 id를 알아서 다른 북마크를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/bookmarks/place/{place_id}")
    public CommonResult deleteBookmarkByPlaceIdV1(
            @Parameter(description = "북마크 정보 내의 장소 id (String)", required = true) @PathVariable("place_id") String placeId) {

        bookmarkService.deleteBookmarkByPlaceId(placeId);

        return responseService.getSuccessResult();
    }

}
