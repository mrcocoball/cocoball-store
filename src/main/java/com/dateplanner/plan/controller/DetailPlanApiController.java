package com.dateplanner.plan.controller;

import com.dateplanner.api.service.ResponseService;
import com.dateplanner.plan.service.DetailPlanApiService;
import com.dateplanner.plan.service.DetailPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "ReviewApiController - 리뷰 기능 API")
@RequiredArgsConstructor
@RestController
public class DetailPlanApiController {

    private DetailPlanApiService detailPlanApiService;
    private DetailPlanService detailPlanService;
    private ResponseService responseService;

}
