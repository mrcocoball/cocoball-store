package dev.be.moduleapi.support.controller;

import dev.be.moduleapi.api.model.PageResult;
import dev.be.moduleapi.api.model.SingleResult;
import dev.be.moduleapi.api.service.ResponseService;
import dev.be.moduleapi.support.dto.FaqCategoryDto;
import dev.be.moduleapi.support.service.FaqApiService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "8. [고객센터 화면 - FAQ - FAQ 리스트, 상세] FaqApiController - FAQ 기능 API")
@Timed("business.controller.faq")
@RequiredArgsConstructor
@RestController
public class FaqApiController {

    private final FaqApiService faqApiService;
    private final ResponseService responseService;


    @Operation(summary = "[GET] FAQ 질문 카테고리 리스트 출력",
            description = "FAQ 질문 카테고리 리스트를 출력합니다. FAQ 질문 카테고리 1건에 해당 카테고리 내의 질문들도 같이 있습니다")
    @GetMapping("/api/v1/faqs")
    public PageResult<FaqCategoryDto> getFaqCategoryListV1(@ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return responseService.getPageResult(faqApiService.getFaqCategoryList(pageable));

    }

    @Operation(summary = "[GET] FAQ 질문 카테고리 + 하위 질문 리스트 출력",
            description = "FAQ 질문 카테고리를 출력합니다. 해당 카테고리와 관련된 질문의 리스트들도 같이 출력됩니다.")
    @GetMapping("/api/v1/faqs/{id}")
    public SingleResult<FaqCategoryDto> getFaqCategoryV1(@Parameter(description = "FAQ 질문 카테고리 ID") @PathVariable("id") Long id) {

        return responseService.getSingleResult(faqApiService.getFaqCategory(id));

    }

}
