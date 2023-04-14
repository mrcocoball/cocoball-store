package dev.be.moduleapi.support.controller;

import dev.be.moduleapi.api.model.CommonResult;
import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.support.dto.AnswerRequestDto;
import dev.be.moduleapi.support.dto.QuestionDto;
import dev.be.moduleapi.support.dto.QuestionRequestDto;
import dev.be.moduleapi.support.service.QnaApiService;
import dev.be.moduleapi.support.service.QnaService;
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
@Tag(name = "8. [고객센터 화면 - 문의사항 - 문의사항 리스트, 상세] QnaApiController - QNA 기능 API")
@Timed("business.controller.qna")
@RequiredArgsConstructor
@RestController
public class QnaApiController {

    private final QnaService qnaService;
    private final QnaApiService qnaApiService;
    private final ResponseService responseService;


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 문의 작성 요청, 사용자 로그인 되어 있어야 함",
            description = "문의 작성을 요청합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 문의를 작성하려는 유저를 체크합니다. <br><br>" +
                    "문의 작성에 필요한 정보는 title, description, categoryId 입니다. <br>" +
                    "여기서 categoryId는 select option으로 받아야 합니다.")
    @PostMapping(value = "/api/v1/questions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveQuestionV1(
            @Parameter(description = "요청한 유저의 인증 정보") Authentication authentication,
            @Parameter(description = "문의 작성 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = QuestionRequestDto.class))) @Valid @RequestBody QuestionRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입

        return responseService.getSingleResult(qnaService.saveQuestion(dto));
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[PUT] 문의 수정, 사용자 로그인 되어 있어야 함",
            description = "문의 ID (id)를 통해 특정 문의의 정보를 수정합니다. <br><br>" +
                    "해당 기능을 사용하기 전에 위의 [GET] /api/v1/questions/{id} 로 수정 전 정보를 가져와야 합니다. 이후 수정 완료 버튼 클릭 시 다음과 같이 요청해야 합니다. <br>" +
                    "[PUT] /api/v1/questions/{id} <br><br>" +
                    "수정되는 정보는 title, description, categoryId 입니다. <br>" +
                    "또한 요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 문의를 수정하려는 유저를 체크합니다.")
    @PutMapping(value = "/api/v1/questions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> updateQuestionV1(
            @Parameter(description = "사용자 정보", required = true) Authentication authentication,
            @Parameter(description = "문의 ID, 문의 리스트 내 문의의 id를 사용합니다", required = true) @PathVariable("id") Long id,
            @Parameter(description = "문의 수정 정보, 수정 버튼 클릭 시 [GET] /api/v1/questions/{id}로 수정 전 정보를 가져와야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = QuestionRequestDto.class))) @Valid @RequestBody QuestionRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입
        dto.setId(id); // 현재 리뷰 ID를 받아서 dto에 주입

        return responseService.getSingleResult(qnaService.updateQuestion(dto));
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 사용자 작성 문의 리스트 출력, 로그인 되어 있어야 함",
            description = "사용자가 작성한 문의 리스트를 출력합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하여 해당 유저가 작성한 문의 리스트를 출력합니다.")
    @GetMapping("/api/v1/questions")
    public PageResult<QuestionDto> getQuestionsByUserNicknameV1(
            @Parameter(description = "요청한 유저의 인증 정보", required = true) Authentication authentication,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();

        return responseService.getPageResult(qnaApiService.getQuestions(nickname, pageable));
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[GET] 문의 ID로 단일 문의 내용 조회",
            description = "문의 ID (id)를 통해 특정 문의의 정보를 가져옵니다. <br><br>" +
                    "사용하는 데이터 : 전부 사용하며 문의 수정 시 필요한 uid (작성자 ID), nickname (작성자 닉네임) 을 비롯한 수정 전 기존 정보를 해당 API로 가져옵니다.")
    @GetMapping("/api/v1/questions/{id}")
    public SingleResult<QuestionDto> getQuestionV1(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable("id") Long id) {

        return responseService.getSingleResult(qnaApiService.getQuestion(id));
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[DELETE] 문의 삭제, 사용자 로그인 되어 있어야 함",
            description = "지정한 문의를 삭제합니다. 삭제하려는 문의의 id를 가져와서 다음과 같이 요청해야 합니다. <br>" +
                    "[DELETE] /api/v1/questions/{id} <br><br>" +
                    "사용자가 문의의 id를 알 가능성은 낮으나, 만약 문의 id를 알아서 다른 문의의를 삭제하려 할 가능성이 있면 로직이 바뀔 수 있습니다.")
    @DeleteMapping("/api/v1/questions/{id}")
    public CommonResult deleteQuestionV1(
            @Parameter(description = "문의 ID, 문의 리스트 내 문의의 id를 사용합니다", required = true) @PathVariable("id") Long id) {

        qnaService.deleteQuestion(id);

        return responseService.getSuccessResult();
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[POST] 문의 내 답변 작성 요청, 사용자 로그인 되어 있어야 함",
            description = "문의 내 답변 작성을 요청합니다. <br>" +
                    "요청 시점에서 요청을 한 유저의 인증 정보를 확인하며, 해당 인증 정보를 토대로 문의를 작성하려는 유저를 체크합니다. <br><br>" +
                    "리뷰 작성에 필요한 정보는 문의 ID(qid), description 입니다. <br>" +
                    "여기서 문의 ID는 프론트엔드에서 요청 시 해당 답변이 작성될 문의 ID(qid)를 넣어줘야 합니다. (문의 화면 내에서 답변 작성)")
    @PostMapping(value = "/api/v1/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> saveAnswerV1(
            @Parameter(description = "요청한 유저의 인증 정보") Authentication authentication,
            @Parameter(description = "답변 작성 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AnswerRequestDto.class))) @Valid @RequestBody AnswerRequestDto dto) {

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setNickname(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입

        return responseService.getSingleResult(qnaService.saveAnswer(dto));
    }

}
