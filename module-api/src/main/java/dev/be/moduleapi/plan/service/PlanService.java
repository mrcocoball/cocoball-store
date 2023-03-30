package dev.be.moduleapi.plan.service;

import dev.be.moduleapi.advice.exception.PlanNotFoundApiException;
import dev.be.moduleapi.advice.exception.UserNotFoundApiException;
import dev.be.moduleapi.plan.dto.PlanRequestDto;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.plan.PlanRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;


    public Long savePlan(PlanRequestDto dto) {

        User user = userRepository.findByNickname(dto.getNickname()).orElseThrow(UserNotFoundApiException::new);

        log.info("[PlanService savePlan] save plan...");

        Plan plan = planRepository.save(dto.toEntity(user, dto.getTitle()));

        log.info("[PlanService savePlan] save plan complete");

        return plan.getId();
    }

    public Long updatePlan(PlanRequestDto dto) {

        Plan plan = planRepository.findById(dto.getId()).orElseThrow(PlanNotFoundApiException::new);

        plan.changeTitle(dto.getTitle());
        plan.changeComment(dto.getComment());

        return plan.getId();
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    public Long finishPlan(Long id) {

        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundApiException::new);
        plan.setFinished();

        return plan.getId();
    }

}
