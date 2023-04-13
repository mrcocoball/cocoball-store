package dev.be.moduleapi.plan.controller;

import dev.be.moduleapi.api.model.CommonResult;
import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.plan.dto.PlanDto;
import dev.be.moduleapi.plan.dto.PlanRequestDto;
import dev.be.moduleapi.plan.service.PlanApiService;
import dev.be.moduleapi.plan.service.PlanService;
import dev.be.modulecore.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "5. [마이 페이지 - 플랜 목록] PlanApiController - 플랜 기능 API")
@RequiredArgsConstructor
@RestController
public class PlanApiController {

    private final PlanApiService planApiService;
    private final PlanService planService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 플랜 작성 요청, 사용자 로그인이 되어야 함",
            description = "플랜 작성을 요청합니다. 이 때 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, <br>" +
                    "해당 인증 정보를 토대로 플랜을 작성하려는 유저를 체크합니다. <br><br>" +
                    "플랜 작성에 필요한 정보는 제목(title) 입니다.")
    @PostMapping(value = "/api/v1/plans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> savePlansV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @Parameter(description = "플랜 작성 정보", required = true) @Valid @RequestBody PlanRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 닉네임을 받아서 dto에 주입

        return responseService.getSingleResult(planService.savePlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 사용자 작성 플랜 리스트 출력, 사용자 로그인이 되어야 함",
            description = "마이페이지 내 내 플랜 화면에서 사용자가 작성한 플랜 리스트를 출력합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 해당 유저가 작성한 플랜 리스트를 출력합니다.")
    @GetMapping("/api/v1/plans")
    public PageResult<PlanDto> getPlansByUserNicknameV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        log.info("authentication, nickname : {}", nickname);

        return responseService.getPageResult(planApiService.getPlanListByNickname(nickname, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 플랜 ID로 플랜 단건 조회, 사용자 로그인이 되어야 함",
            description = "플랜 ID (id)를 통해 특정 플랜의 정보와, 플랜 내의 세부 플랜(목적지) 리스트를 가져옵니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 플랜 수정 시 필요한 uid (작성자 ID), id (플랜 ID) 를 비롯한 수정 전 기존 정보를 해당 API로 가져옵니다.")
    @GetMapping("/api/v1/plans/{id}")
    public SingleResult<PlanDto> getPlanV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @Parameter(description = "플랜 ID", required = true) @PathVariable("id") Long id) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        return responseService.getSingleResult(planApiService.getPlan(id, nickname));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[PUT] 플랜 수정, 사용자 로그인이 되어야 하며 플랜 ID 필요",
            description = "플랜 ID (id)를 통해 특정 플랜의 정보를 수정합니다. <br>" +
                    "해당 기능을 사용하기 전에 위의 [GET] /api/v1/plans/{id} 로 수정 전 정보를 가져와야 합니다. 이후 수정 완료 버튼 클릭 시 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] /api/v1/plans/{id} <br><br>" +
                    "수정되는 정보는 title 이며, 플랜이 완료된 상태라면 comment도 수정 가능합니다. 플랜 내의 세부 플랜(목적지) 리스트에 대한 수정은 <br>" +
                    "세부 플랜 클릭 시 이벤트를 통해 세부 플랜의 API를 활용해서 수정할 수 있도록 합니다 <br>" +
                    "(예시 : 플랜 정보 조회로 세부 플랜(id = 1, id = 2) 2개가 같이 나오고, 이 중에서 id = 1인 세부 플랜 클릭 시 [GET] api/v1/detailPlans/{1}...<br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 플랜을 수정하려는 유저를 체크합니다.")
    @PutMapping(value = "/api/v1/plans/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updatePlanV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @Parameter(description = "플랜 ID", required = true) @PathVariable("id") Long id,
            @Parameter(description = "플랜 수정 정보, 수정 버튼 클릭 시 [GET] /api/v1/plans/{id}로 수정 전 정보를 가져와야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PlanRequestDto.class))) @Valid @RequestBody PlanRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입
        dto.setId(id); // 현재 플랜 ID를 받아서 dto에 주입

        return responseService.getSingleResult(planService.updatePlan(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 플랜 삭제, 사용자 로그인이 되어야 하며 플랜 ID 필요",
            description = "지정한 플랜을 삭제합니다. 삭제하려는 플랜의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/plans/{id} <br><br>" +
                    "사용자가 플랜의 id를 알 가능성은 낮으나, 만약 플랜 id를 알아서 다른 리뷰를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/plans/{id}")
    public CommonResult deletePlanV1(
            @Parameter(description = "플랜 ID", required = true) @PathVariable("id") Long id) {

        planService.deletePlan(id);

        return responseService.getSuccessResult();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[PUT] 플랜 완료 처리, 사용자 로그인이 되어야 하며 플랜 ID 필요",
            description = "지정한 플랜을 완료 처리합니다. 완료 처리하려는 플랜의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] /api/v1/plans/{id}/finish <br><br>" +
                    "완료 처리를 할 경우 finished 상태가 true로 변경되며, 이 상태의 조건을 체크하여 comment를 작성할 수 있도록 해주세요. <br>" +
                    "사용자가 플랜의 id를 알 가능성은 낮으나, 만약 플랜 id를 알아서 다른 리뷰를 삭제하려 할 가능성이 있다면 로직이 바뀔 수 있습니다.")
    @PutMapping("/api/v1/plans/{id}/finish")
    public SingleResult<Long> finishPlanV1(
            @Parameter(description = "플랜 ID", required = true) @PathVariable("id") Long id) {

        return responseService.getSingleResult(planService.finishPlan(id));
    }

}
