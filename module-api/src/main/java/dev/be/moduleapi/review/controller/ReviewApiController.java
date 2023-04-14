package dev.be.moduleapi.review.controller;

import dev.be.moduleapi.api.model.CommonResult;
import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.image.service.FileService;
import dev.be.moduleapi.review.dto.ReviewDto;
import dev.be.moduleapi.review.dto.ReviewRequestDto;
import dev.be.moduleapi.review.service.ReviewApiService;
import dev.be.moduleapi.review.service.ReviewService;
import dev.be.modulecore.domain.user.User;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "2. [장소 검색 화면 - 장소 상세 정보 - 리뷰 리스트, 리뷰 상세] ReviewApiController - 리뷰 기능 API")
@Timed("business.controller.review")
@RequiredArgsConstructor
@RestController
public class ReviewApiController {

    private final ReviewApiService reviewApiService;
    private final ReviewService reviewService;
    private final FileService fileService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 리뷰 작성 요청, 사용자 로그인 되어 있어야 함",
            description = "특정 장소에 리뷰 작성을 요청합니다. 이 때 특정 장소의 place_id를 사용해야 합니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 리뷰를 작성하려는 유저를 체크합니다. <br><br>" +
                    "리뷰 작성에 필요한 정보는 place_id, title, description, review_score, List <String> filenames 입니다. <br>" +
                    "여기서 List<String>filenames의 경우 첨부 이미지의 주소이며, 여기에는 이미지 업로드 API를 통해 변환된 것을 넣어야 합니다.")
    @PostMapping(value = "/api/v1/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveReviewV1(
            @Parameter(description = "요청한 유저의 인증 정보") Authentication authentication,
            @Parameter(description = "리뷰 작성 정보, 리뷰를 작성하려는 장소의 장소 ID(place_id)는 프론트엔드에서 가져와서 요청 시 넣어줘야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReviewRequestDto.class))) @Valid @RequestBody ReviewRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입

        return responseService.getSingleResult(reviewService.saveReview(dto));
    }

    @Operation(summary = "[GET] 장소 내의 리뷰 리스트 출력",
            description = "특정 장소에 작성된 리뷰 리스트를 출력합니다. 장소 상세 정보 조회 시 해당 API도 같이 호출되어야 합니다. <br>" +
                    "특정 장소의 장소 ID (place_id) 가 필요합니다.")
    @GetMapping("/api/v1/reviews/list/{place_id}")
    public PageResult<ReviewDto> getReviewsByPlaceIdV1(
            @Parameter(description = "장소 ID", required = true) @PathVariable("place_id") String placeId,
            @ParameterObject @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return responseService.getPageResult(reviewApiService.getReviewListByPlaceId(placeId, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 사용자 작성 리뷰 리스트 출력, 로그인 되어 있어야 함",
            description = "마이페이지 내 내 리뷰 화면에서 사용자가 작성한 리뷰 리스트를 출력합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 해당 유저가 작성한 리뷰 리스트를 출력합니다. <br><br>" +
                    "장소 상세 정보 화면에서 보는 것이 아닌 마이페이지에서 보는 것이라 <br>" +
                    "리스트 내 리뷰 조회 시 화면 처리에 대해서는 논의가 필요합니다. (리뷰만 띄울지, 리뷰 + 장소 같이 띄울지)")
    @GetMapping("/api/v1/reviews")
    public PageResult<ReviewDto> getReviewsByUserNicknameV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        log.info("authentication, nickname : {}", nickname);

        return responseService.getPageResult(reviewApiService.getReviewListByNickname(nickname, pageable));
    }

    @Operation(summary = "[GET] 리뷰 ID로 단일 리뷰 조회",
            description = "리뷰 ID (id)를 통해 특정 리뷰의 정보를 가져옵니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 리뷰 수정 시 필요한 uid (작성자 ID), pid (장소 ID, PK) 를 비롯한 수정 전 기존 정보를 해당 API로 가져옵니다.")
    @GetMapping("/api/v1/reviews/{id}")
    public SingleResult<ReviewDto> getReviewV1(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id) {

        return responseService.getSingleResult(reviewApiService.getReview(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[PUT] 리뷰 수정, 사용자 로그인 되어 있어야 함",
            description = "리뷰 ID (id)를 통해 특정 리뷰의 정보를 수정합니다. <br><br>" +
                    "해당 기능을 사용하기 전에 위의 [GET] /api/v1/reviews/{id} 로 수정 전 정보를 가져와야 합니다. 이후 수정 완료 버튼 클릭 시 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] /api/v1/reviews/{id} <br><br>" +
                    "수정되는 정보는 title, description, review_score, List<String>filenames 입니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 리뷰를 수정하려는 유저를 체크합니다.")
    @PutMapping(value = "/api/v1/reviews/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateReviewV1(
            @Parameter(description = "사용자 정보", required = true) Authentication authentication,
            @Parameter(description = "리뷰 ID, 리뷰 리스트 내 리뷰의 id를 사용합니다", required = true) @PathVariable("id") Long id,
            @Parameter(description = "리뷰 수정 정보, 수정 버튼 클릭 시 [GET] /api/v1/reviews/{id}로 수정 전 정보를 가져와야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReviewRequestDto.class))) @Valid @RequestBody ReviewRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입
        dto.setId(id); // 현재 리뷰 ID를 받아서 dto에 주입

        return responseService.getSingleResult(reviewService.updateReview(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 리뷰 삭제, 사용자 로그인 되어 있어야 함",
            description = "지정한 리뷰를 삭제합니다. 삭제하려는 리뷰의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/reviews/{id} <br><br>" +
                    "사용자가 리뷰의 id를 알 가능성은 낮으나, 만약 리뷰 id를 알아서 다른 리뷰를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/reviews/{id}")
    public CommonResult deleteReviewV1(
            @Parameter(description = "리뷰 ID, 리뷰 리스트 내 리뷰의 id를 사용합니다", required = true) @PathVariable("id") Long id) {

        ReviewDto dto = reviewApiService.getReview(id);
        reviewService.deleteReview(id);
        fileService.removeFilesInDto(dto);

        return responseService.getSuccessResult();
    }

}
