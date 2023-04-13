package dev.be.moduleapi.plan.controller;

import dev.be.moduleapi.api.model.CommonResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.plan.dto.DetailPlanDto;
import dev.be.moduleapi.plan.dto.DetailPlanRequestDto;
import dev.be.moduleapi.plan.service.DetailPlanApiService;
import dev.be.moduleapi.plan.service.DetailPlanService;
import dev.be.modulecore.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "6. [마이 페이지 - 플랜 목록 - 플랜 세부 정보] DetailPlanApiController - 세부 플랜 기능 API")
@RequiredArgsConstructor
@RestController
public class DetailPlanApiController {

    private final DetailPlanApiService detailPlanApiService;
    private final DetailPlanService detailPlanService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 세부 플랜(목적지) 설정 요청, 사용자 로그인이 되어야 함",
            description = "특정 플랜 내부에 세부 플랜(목적지) 설정을 요청합니다. 이 때 특정 플랜의 ID (pid)를 사용해야 하며, 다음과 같이 요청해야 합니다. <br>" +
                    "[POST] /api/v1/detailPlans?planId={pid} <br><br>" +
                    "또한, 세부 플랜(목적지) 설정에 필요한 정보는 place_id(장소 ID), ord(순서) 입니다. <br>" +
                    "여기서 목적지로 설정할 장소 ID place_id는 다음 방식 중 한 가지로 가져옵니다. <br>" +
                    "1. 사용자가 장소 검색 기능을 활용하여 최종적으로 선택한 장소에서 place_id를 가져오기<br>" +
                    "2. 사용자가 북마크에 저장된 장소들을 확인하여 최종적으로 선택한 장소에서 place_id를 가져오기")
    @PostMapping(value = "/api/v1/detailPlans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveDetailPlansV1(
            @Parameter(description = "플랜 ID, 세부 플랜(목적지)를 설정하려는 플랜의 플랜 ID(pid)는 프론트엔드에서 가져와서 요청 시 넣어줘야 합니다.",
                    required = true) @RequestParam Long planId,
            @Parameter(description = "세부 플랜 작성 정보, 목적지 장소의 place_id는 위에서 설명한 방식으로 프론트엔드에서 가져와서 요청 시 넣어줘야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DetailPlanRequestDto.class))) @Valid @RequestBody DetailPlanRequestDto dto) {

        dto.setPid(planId);

        return responseService.getSingleResult(detailPlanService.saveDetailPlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 세부 플랜(목적지) ID로 단건 조회, 사용자 로그인이 되어야 하며 플랜 ID 필요, 플랜 작성자일 경우에만 조회 가능",
            description = "세부 플랜(목적지) ID (id)를 통해 특정 세부 플랜의 정보를 가져옵니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 접근 권한을 체크합니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 세부 플랜 수정 시 필요한 place_id(장소 ID), ord(순서) 를 비롯한 수정 전 기존 정보를 해당 API로 가져옵니다.")
    @GetMapping("/api/v1/detailPlans/{id}")
    public SingleResult<DetailPlanDto> getDetailPlanV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @Parameter(description = "세부 플랜(목적지) ID", required = true) @PathVariable("id") Long id) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        return responseService.getSingleResult(detailPlanApiService.getDetailPlan(id, nickname));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[PUT] 세부 플랜(목적지) 수정, 사용자 로그인이 되어야 하며 세부 플랜 ID 필요",
            description = "세부 플랜(목적지) ID (id)를 통해 특정 세부 플랜의 정보를 수정합니다. <br><br>" +
                    "해당 기능을 사용하기 전에 위의 [GET] /api/v1/detailPlans/{id} 로 수정 전 정보를 가져와야 합니다. 이후 수정 완료 버튼 클릭 시 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] /api/v1/detailPlans/{id} <br><br>" +
                    "수정되는 정보는 place_id, ord 입니다.")
    @PutMapping(value = "/api/v1/detailPlans/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateDetailPlanV1(
            @Parameter(description = "세부 플랜(목적지) ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "세부 플랜(목적지) 수정 정보, 수정 버튼 클릭 시 [GET] /api/v1/detailPlans{id}로 수정 전 정보를 가져와야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DetailPlanRequestDto.class))) @Valid @RequestBody DetailPlanRequestDto dto) {

        dto.setId(id); // 현재 세부 플랜 ID를 받아서 dto에 주입

        return responseService.getSingleResult(detailPlanService.updateDetailPlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 세부 플랜(목적지) 삭제, 사용자 로그인이 되어야 하며 세부 플랜 ID 필요",
            description = "지정한 세부 플랜(목적지)를 삭제합니다. 삭제하려는 세부 플랜의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/detailPlans/{id} <br><br>" +
                    "사용자가 세부 플랜의 id를 알 가능성은 낮으나, 만약 세부 플랜 id를 알아서 다른 세부 플랜 id를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/detailPlans/{id}")
    public CommonResult deleteDetailPlanV1(
            @Parameter(description = "세부 플랜 ID", required = true) @PathVariable("id") Long id) {

        detailPlanService.deleteDetailPlan(id);

        return responseService.getSuccessResult();
    }
}
