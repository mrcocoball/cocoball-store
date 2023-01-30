package com.dateplanner.plan.service;

import com.dateplanner.advice.exception.PlanNotFoundApiException;
import com.dateplanner.api.PaginationService;
import com.dateplanner.plan.dto.PlanDto;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.plan.repository.PlanRepository;
import com.dateplanner.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlanApiService {

    private final PlanRepository planRepository;
    private final PaginationService paginationService;


    public Page<PlanDto> getPlanListByUid(String uid, Pageable pageable) {

        log.info("[PlanApiService getPlanList] get plan list start...");
        List<PlanDto> dtos = planRepository.findByUser_Uid(uid).stream().map(PlanDto::from).collect(Collectors.toList());
        log.info("[PlanApiService getPlanList] get plan list complete, size : {}", dtos.size());

        return paginationService.listToPage(dtos, pageable);
    }

    public PlanDto getPlan(Long id, String uid) {

        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundApiException::new);
        String userId = plan.getUser().getUid();

        if (!uid.equals(userId)) { throw new AccessDeniedException("접근 권한이 없습니다."); }

        return planRepository.findById(id).map(PlanDto::from).orElseThrow(PlanNotFoundApiException::new);
    }

}
