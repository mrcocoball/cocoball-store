package com.dateplanner.plan.service;

import com.dateplanner.advice.exception.DetailPlanNotFoundApiException;
import com.dateplanner.plan.dto.DetailPlanDto;
import com.dateplanner.plan.entity.DetailPlan;
import com.dateplanner.plan.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DetailPlanApiService {

    private final DetailPlanRepository detailPlanRepository;


    public DetailPlanDto getDetailPlan(Long id, String uid) {

        DetailPlan detailPlan = detailPlanRepository.findById(id).orElseThrow(DetailPlanNotFoundApiException::new);
        String userId = detailPlan.getPlan().getUser().getUid();

        if (!uid.equals(userId)) { throw new AccessDeniedException("접근 권한이 없습니다."); }

        return detailPlanRepository.findById(id).map(DetailPlanDto::from).orElseThrow(DetailPlanNotFoundApiException::new);
    }
}
