package com.dateplanner.plan.service;

import com.dateplanner.advice.exception.PlanNotFoundApiException;
import com.dateplanner.api.PaginationService;
import com.dateplanner.plan.dto.PlanDto;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.plan.repository.PlanRepository;
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

        log.info("[PlanApiService getPlan] N+1 Query?"); // 조회만 해올 때는 N+1 쿼리 안 나감 (상위 엔티티인 사용자를 fetch 조인해서 한번에 가져오므로)
        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundApiException::new);
        String userId = plan.getUser().getUid();

        if (!uid.equals(userId)) { throw new AccessDeniedException("접근 권한이 없습니다."); }

        log.info("[PlanApiService getPlan] N+1 Query?"); // return 과정에서 하위에 얽힌 DetailPlan을 가져오는 쿼리 발생
        return PlanDto.from(plan);
    }

}
