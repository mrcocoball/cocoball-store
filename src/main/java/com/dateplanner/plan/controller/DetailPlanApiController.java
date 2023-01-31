package com.dateplanner.plan.controller;

import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.plan.dto.DetailPlanDto;
import com.dateplanner.plan.dto.DetailPlanRequestDto;
import com.dateplanner.plan.dto.PlanDto;
import com.dateplanner.plan.dto.PlanRequestDto;
import com.dateplanner.plan.service.DetailPlanApiService;
import com.dateplanner.plan.service.DetailPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "DetailPlanApiController - 세부 플랜 기능 API")
@RequiredArgsConstructor
@RestController
public class DetailPlanApiController {

    private final DetailPlanApiService detailPlanApiService;
    private final DetailPlanService detailPlanService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "세부 플랜 작성, 사용자 로그인이 되어야 함", description = "[POST] 세부 플랜 작성")
    @PostMapping(value = "/api/v1/detailPlans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveDetailPlansV1(
            @Parameter(description = "플랜 ID, 프론트엔드에서 플랜 ID 넣어줄 것", required = true) @RequestParam Long planId,
            @Parameter(description = "세부 플랜 작성 정보, 플랜 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody DetailPlanRequestDto dto) {

        /**
         * DetailPlanRequestDto의 경우, 장소 데이터 (kpid, addressname, placename, longitude, latitude은
         * 프론트엔드에서 사용자에게 다음 REST API를 호출해서 나온 결과를 선택하게 해서 선택한 장소의 값을 넣어줘야 한다
         * 1. 장소 검색하여 선택 (/api/v1/places)
         * 2. 북마크에서 가져오기 (/api/v1/bookmarks)
         */

        dto.setPid(planId);

        return responseService.getSingleResult(detailPlanService.saveDetailPlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "세부 플랜 단건 조회, 사용자 로그인이 되어야 하며 플랜 ID 필요, 플랜 작성자일 경우에만 조회 가능", description = "[GET] 플랜 단건 조회")
    @GetMapping("/api/v1/detailPlans/{id}")
    public SingleResult<DetailPlanDto> getDetailPlanV1(
            @Parameter(description = "사용자 정보", required = true) Principal principal,
            @Parameter(description = "세부 플랜 ID", required = true) @PathVariable("id") Long id) {

        String uid = principal.getName();

        return responseService.getSingleResult(detailPlanApiService.getDetailPlan(id, uid));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "세부 플랜 수정, 사용자 로그인이 되어야 하며 세부 플랜 ID 필요", description = "[PUT] 세부 플랜 수정")
    @PutMapping(value = "/api/v1/detailPlans/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateDetailPlanV1(
            @Parameter(description = "세부 플랜 ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "세부 플랜 작성 정보, 장소 ID는 프론트엔드에서 가져와야 함", required = true) @Valid @RequestBody DetailPlanRequestDto dto) {

        /**
         * 여기서는 사용자 ID(uid), 세부 플랜 ID를 서버에서 직접 주입해주고 있으나
         * 가능하다면 프론트엔드에서 현재 보고 있는 사용자 ID(uid), title를 받아서 dto에 주입해줘야 함
         * 위의 단건 조회 API를 통해 수정 전 데이터를 가져오고 처리해야 한다
         */

        dto.setId(id); // 현재 세부 플랜 ID를 받아서 dto에 주입

        return responseService.getSingleResult(detailPlanService.updateDetailPlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Parameters({
            @Parameter(
                    name = "X-AUTH-TOKEN",
                    description = "로그인 성공 후 AccessToken",
                    required = true, in = ParameterIn.HEADER
            )
    })
    @Operation(summary = "세부 플랜 삭제, Dto의 uid와 사용자 인증 정보의 uid 일치 여부 확인 필요", description = "[DELETE] 세부 플랜 삭제")
    @DeleteMapping("/api/v1/detailPlans/{id}")
    public CommonResult deleteDetailPlanV1(
            @Parameter(description = "세부 플랜 ID, 프론트엔드에서 세부 플랜 ID 넣어줄 것", required = true) @PathVariable("id") Long id) {

        /**
         * 프론트엔드에서 삭제 버튼을 인증 상태에 따라 표시하고
         * 그 과정에서 플랜 Dto의 uid와 현재 사용자 인증 정보의 uid 비교 가능하다면 그대로 진행해도 되나
         * 그러지 않을 경우엔 principal을 파라미터로 받고
         * 사용자 정보 uid를 토대로 DB에서 삭제하려는 플랜의 uid와 비교를 한 후 삭제 처리를 해야할 것으로 보임
         */

        detailPlanService.deleteDetailPlan(id);

        return responseService.getSuccessResult();
    }
}
