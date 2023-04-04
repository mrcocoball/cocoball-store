package dev.be.moduleapi.plan.service;

import dev.be.moduleapi.advice.exception.PlanNotFoundApiException;
import dev.be.moduleapi.plan.dto.PlanDto;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.repositories.plan.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlanApiService {

    private final PlanRepository planRepository;


    public Page<PlanDto> getPlanListByNickname(String nickname, Pageable pageable) {

        log.info("[PlanApiService getPlanList] get plan list start...");
        Page<PlanDto> dtos = planRepository.findByUser_Nickname(nickname, pageable).map(plan -> PlanDto.from(plan));
        log.info("[PlanApiService getPlanList] get plan list complete, size : {}", dtos.getSize());

        return dtos;
    }

    public PlanDto getPlan(Long id, String nickname) {

        log.info("[PlanApiService getPlan] N+1 Query?"); // 조회만 해올 때는 N+1 쿼리 안 나감 (상위 엔티티인 사용자를 fetch 조인해서 한번에 가져오므로)
        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundApiException::new);
        String userNickname = plan.getUser().getNickname();

        if (!nickname.equals(userNickname)) { throw new AccessDeniedException("접근 권한이 없습니다."); }

        log.info("[PlanApiService getPlan] N+1 Query?"); // return 과정에서 하위에 얽힌 DetailPlan을 가져오는 쿼리 발생
        return PlanDto.from(plan);
    }

}
