package com.dateplanner.review.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.review.dto.ReviewDto;
import com.dateplanner.review.dto.ReviewRequestDto;
import com.dateplanner.review.service.ReviewApiService;
import com.dateplanner.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@Tag(name = "ReviewApiController - 리뷰 기능 API")
@RequiredArgsConstructor
@RestController
public class ReviewApiController {

    private final ReviewApiService reviewApiService;
    private final ReviewService reviewService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "리뷰 작성, 사용자 로그인이 되어야 함", description = "[POST] 리뷰 작성")
    @PostMapping(value = "/api/v1/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveReviewV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 작성 정보, 장소 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody ReviewRequestDto dto) {

        dto.setUid(principal.getName()); // 현재 사용자 정보를 받아서 dto에 주입

        return responseService.getSingleResult(reviewService.saveReview(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "장소 내 리뷰 리스트 조회, 사용자 로그인이 되어야 하며 장소 정보 ID 필요", description = "[GET] 장소 내의 리뷰 리스트 조회")
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
    @Operation(summary = "사용자가 작성한 리뷰 리스트 조회, 사용자 로그인이 되어야 하며 사용자 정보 필요", description = "[GET] 사용자 작성 리뷰 리스트 조회")
    @GetMapping("/api/v1/reviews/")
    public PageResult<ReviewDto> getReviewsByUserIdV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        String uid = principal.getName();
        ;
        log.info("authentication, uid : {}", uid);

        return responseService.getPageResult(reviewApiService.getReviewListByUid(uid, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "리뷰 단건 조회, 사용자 로그인이 되어야 하며 리뷰 ID 필요", description = "[GET] 리뷰 단건 조회")
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
    @Operation(summary = "리뷰 수정, 사용자 로그인이 되어야 하며 작성자와 동일해야 함", description = "[PUT] 리뷰 수정")
    @PutMapping(value = "/api/v1/reviews/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateReviewV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "리뷰 작성 정보, 장소 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody ReviewRequestDto dto) {

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
    @Operation(summary = "리뷰 삭제, Dto의 uid와 사용자 인증 정보의 uid 일치 여부 확인 필요", description = "[DELETE] 리뷰 삭제")
    @DeleteMapping("/api/v1/reviews/{id}")
    public CommonResult deleteReviewV1(
            @Parameter(description = "리뷰 ID, 프론트엔드에서 리뷰 ID 넣어줄 것", required = true) @PathVariable("id") Long id) {

        /**
         * 프론트엔드에서 삭제 버튼을 인증 상태에 따라 표시하고
         * 그 과정에서 리뷰 Dto의 uid와 현재 사용자 인증 정보의 uid 비교 가능하다면 그대로 진행해도 되나
         * 그러지 않을 경우엔 principal을 파라미터로 받고
         * 사용자 정보 uid를 토대로 DB에서 삭제하려는 북마크의 uid와 비교를 한 후 삭제 처리를 해야할 것으로 보임
         */

        reviewService.deleteReview(id);

        return responseService.getSuccessResult();
    }
}
