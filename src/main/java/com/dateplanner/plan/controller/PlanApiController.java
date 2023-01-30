package com.dateplanner.plan.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.PageResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.plan.dto.PlanDto;
import com.dateplanner.plan.dto.PlanRequestDto;
import com.dateplanner.plan.service.PlanApiService;
import com.dateplanner.plan.service.PlanService;
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
@Tag(name = "PlanApiController - 플랜 기능 API")
@RequiredArgsConstructor
@RestController
public class PlanApiController {

    private final PlanApiService planApiService;
    private final PlanService planService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "플랜 작성, 사용자 로그인이 되어야 함", description = "[POST] 플랜 작성")
    @PostMapping(value = "/api/v1/plans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> savePlansV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 작성 정보, 장소 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody PlanRequestDto dto) {

        dto.setUid(principal.getName()); // 현재 사용자 정보를 받아서 dto에 주입

        return responseService.getSingleResult(planService.savePlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "사용자가 작성한 플랜 리스트 조회, 사용자 로그인이 되어야 하며 사용자 정보 필요", description = "[GET] 사용자 작성 플랜 리스트 조회")
    @GetMapping("/api/v1/plans")
    public PageResult<PlanDto> getPlansByUserIdV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        String uid = principal.getName();

        log.info("authentication, uid : {}", uid);

        return responseService.getPageResult(planApiService.getPlanListByUid(uid, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "플랜 단건 조회, 사용자 로그인이 되어야 하며 플랜 ID 필요, 플랜 작성자일 경우에만 조회 가능", description = "[GET] 플랜 단건 조회")
    @GetMapping("/api/v1/plans/{id}")
    public SingleResult<PlanDto> getPlanV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id) {

        String uid = principal.getName();

        return responseService.getSingleResult(planApiService.getPlan(id, uid));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "플랜 수정, 사용자 로그인이 되어야 하며 플랜 ID 필요, 작성자와 동일해야 함", description = "[PUT] 플랜 수정")
    @PutMapping(value = "/api/v1/plans/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updatePlanV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "리뷰 작성 정보, 장소 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody PlanRequestDto dto) {

        /**
         * 여기서는 사용자 ID(uid), 리뷰 ID를 서버에서 직접 주입해주고 있으나
         * 가능하다면 프론트엔드에서 현재 보고 있는 사용자 ID(uid), title를 받아서 dto에 주입해줘야 함
         */

        dto.setUid(principal.getName()); // 현재 사용자 정보를 받아서 dto에 주입
        dto.setId(id); // 현재 플랜 ID를 받아서 dto에 주입

        return responseService.getSingleResult(planService.updatePlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "플랜 삭제, Dto의 uid와 사용자 인증 정보의 uid 일치 여부 확인 필요", description = "[DELETE] 플랜 삭제")
    @DeleteMapping("/api/v1/plans/{id}")
    public CommonResult deletePlanV1(
            @Parameter(description = "플랜 ID, 프론트엔드에서 플랜 ID 넣어줄 것", required = true) @PathVariable("id") Long id) {

        /**
         * 프론트엔드에서 삭제 버튼을 인증 상태에 따라 표시하고
         * 그 과정에서 플랜 Dto의 uid와 현재 사용자 인증 정보의 uid 비교 가능하다면 그대로 진행해도 되나
         * 그러지 않을 경우엔 principal을 파라미터로 받고
         * 사용자 정보 uid를 토대로 DB에서 삭제하려는 플랜의 uid와 비교를 한 후 삭제 처리를 해야할 것으로 보임
         */

        planService.deletePlan(id);

        return responseService.getSuccessResult();
    }

}
