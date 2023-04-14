package dev.be.moduleapi.plan.service;

import dev.be.moduleapi.advice.exception.DetailPlanNotFoundApiException;
import dev.be.moduleapi.plan.dto.DetailPlanDto;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@Timed("business.service.detail_plan")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DetailPlanApiService {

    private final DetailPlanRepository detailPlanRepository;


    public DetailPlanDto getDetailPlan(Long id, String nickname) {

        DetailPlan detailPlan = detailPlanRepository.findById(id).orElseThrow(DetailPlanNotFoundApiException::new);
        String userNickname = detailPlan.getPlan().getUser().getNickname();

        if (!nickname.equals(userNickname)) { throw new AccessDeniedException("접근 권한이 없습니다."); }

        return detailPlanRepository.findById(id).map(DetailPlanDto::from).orElseThrow(DetailPlanNotFoundApiException::new);
    }
}
