package com.dateplanner.review.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.image.service.FileService;
import com.dateplanner.review.dto.ReviewDto;
import com.dateplanner.review.dto.ReviewRequestDto;
import com.dateplanner.review.service.ReviewApiService;
import com.dateplanner.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "1. [장소 검색 화면 - 장소 상세 정보 - 리뷰 리스트, 리뷰 상세] ReviewApiController - 리뷰 기능 API")
@RequiredArgsConstructor
@RestController
public class ReviewApiController {

    private final ReviewApiService reviewApiService;
    private final ReviewService reviewService;
    private final FileService fileService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[POST] 리뷰 작성 요청, 사용자 로그인 되어 있어야 함",
            description = "특정 장소에 리뷰 작성을 요청합니다. 이 때 특정 장소의 place_id를 사용해야 합니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 리뷰를 작성하려는 유저를 체크합니다. <br><br>" +
                    "리뷰 작성에 필요한 정보는 place_id, title, description, review_score, List<String>filenames 입니다. <br>" +
                    "여기서 List<String>filenames의 경우 첨부 이미지의 주소이며, 여기에는 이미지 업로드 API를 통해 변환된 것을 넣어야 합니다.")
    @PostMapping(value = "/api/v1/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveReviewV1(
            @Parameter(description = "요청한 유저의 인증 정보") Principal principal,
            @Parameter(description = "리뷰 작성 정보, 리뷰를 작성하려는 장소의 장소 ID(place_id)는 프론트엔드에서 가져와서 요청 시 넣어줘야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReviewRequestDto.class))) @Valid @RequestBody ReviewRequestDto dto) {

        dto.setUid(principal.getName()); // 현재 사용자 정보를 받아서 dto에 주입

        return responseService.getSingleResult(reviewService.saveReview(dto));
    }

    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[GET] 장소 내의 리뷰 리스트 출력",
            description = "특정 장소에 작성된 리뷰 리스트를 출력합니다. 장소 상세 정보 조회 시 해당 API도 같이 호출되어야 합니다. <br>" +
                    "특정 장소의 장소 ID (place_id) 가 필요합니다.")
    @GetMapping("/api/v1/reviews/list/{place_id}")
    public PageResult<ReviewDto> getReviewsByPlaceIdV1(
            @Parameter(description = "장소 ID", required = true) @PathVariable("place_id") String placeId,
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(reviewApiService.getReviewListByPlaceId(placeId, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[GET] 사용자 작성 리뷰 리스트 출력, 로그인 되어 있어야 함",
            description = "마이페이지 내 내 리뷰 화면에서 사용자가 작성한 리뷰 리스트를 출력합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 해당 유저가 작성한 리뷰 리스트를 출력합니다. <br><br>" +
                    "장소 상세 정보 화면에서 보는 것이 아닌 마이페이지에서 보는 것이라 <br>" +
                    "리스트 내 리뷰 조회 시 화면 처리에 대해서는 논의가 필요합니다. (리뷰만 띄울지, 리뷰 + 장소 같이 띄울지)")
    @GetMapping("/api/v1/reviews/")
    public PageResult<ReviewDto> getReviewsByUserIdV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        String uid = principal.getName();
        ;
        log.info("authentication, uid : {}", uid);

        return responseService.getPageResult(reviewApiService.getReviewListByUid(uid, pageable));
    }

    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[GET] 리뷰 ID로 단일 리뷰 조회",
            description = "리뷰 ID (id)를 통해 특정 리뷰의 정보를 가져옵니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 리뷰 수정 시 필요한 uid (작성자 ID), pid (장소 ID, PK) 를 비롯한 수정 전 기존 정보를 해당 API로 가져옵니다.")
    @GetMapping("/api/v1/reviews/{id}")
    public SingleResult<ReviewDto> getReviewV1(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id) {

        return responseService.getSingleResult(reviewApiService.getReview(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[PUT] 리뷰 수정, 사용자 로그인 되어 있어야 함",
            description = "리뷰 ID (id)를 통해 특정 리뷰의 정보를 수정합니다. <br><br>" +
                    "해당 기능을 사용하기 전에 위의 [GET] api/v1/reviews/{id} 로 수정 전 정보를 가져와야 합니다. 이후 수정 버튼 클릭 시 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] api/v1/reviews/{id} <br><br>" +
                    "수정되는 정보는 title, description, review_score, List<String>filenames 입니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 리뷰를 수정하려는 유저를 체크합니다.")
    @PutMapping(value = "/api/v1/reviews/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateReviewV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 ID, 리뷰 리스트 내 리뷰의 id를 사용합니다", required = true) @PathVariable("id") Long id,
            @Parameter(description = "리뷰 수정 정보, 수정 버튼 클릭 시 [GET] api/v1/reviews{id}로 수정 전 정보를 가져와야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReviewRequestDto.class))) @Valid @RequestBody ReviewRequestDto dto) {

        /**
         * 여기서는 사용자 ID(uid), 리뷰 ID를 서버에서 직접 주입해주고 있으나
         * 가능하다면 프론트엔드에서 현재 보고 있는 사용자 ID(uid), placeId, title, description, score를 받아서 dto에 주입해줘야 함
         */

        dto.setUid(principal.getName()); // 현재 사용자 정보를 받아서 dto에 주입
        dto.setId(id); // 현재 리뷰 ID를 받아서 dto에 주입

        return responseService.getSingleResult(reviewService.updateReview(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "[DELETE] 리뷰 삭제, 사용자 로그인 되어 있어야 함",
            description = "지정한 리뷰를 삭제합니다. 삭제하려는 리뷰의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] api/v1/reviews/{id} <br><br>" +
                    "사용자가 리뷰의 id를 알 가능성은 낮으나, 만약 리뷰 id를 알아서 다른 리뷰를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/reviews/{id}")
    public CommonResult deleteReviewV1(
            @Parameter(description = "리뷰 ID, 리뷰 리스트 내 리뷰의 id를 사용합니다", required = true) @PathVariable("id") Long id) {

        /**
         * 프론트엔드에서 삭제 버튼을 인증 상태에 따라 표시하고
         * 그 과정에서 리뷰 Dto의 uid와 현재 사용자 인증 정보의 uid 비교 가능하다면 그대로 진행해도 되나
         * 그러지 않을 경우엔 principal을 파라미터로 받고
         * 사용자 정보 uid를 토대로 DB에서 삭제하려는 북마크의 uid와 비교를 한 후 삭제 처리를 해야할 것으로 보임
         */

        ReviewDto dto = reviewApiService.getReview(id);
        reviewService.deleteReview(id);
        fileService.removeFilesInDto(dto);

        return responseService.getSuccessResult();
    }

}
