package com.dateplanner.plan.service;

import com.dateplanner.advice.exception.PlanNotFoundApiException;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.plan.dto.PlanRequestDto;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.plan.repository.DetailPlanRepository;
import com.dateplanner.plan.repository.PlanRepository;
import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
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
    private final DetailPlanRepository detailPlanRepository;


    public Long savePlan(PlanRequestDto dto) {

        User user = userRepository.findByUid(dto.getUid()).orElseThrow(UserNotFoundApiException::new);

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
        detailPlanRepository.deleteByPlan_Id(id);
    }

    public Long finishPlan(Long id) {

        Plan plan = planRepository.findById(id).orElseThrow(PlanNotFoundApiException::new);
        plan.setFinished();

        return plan.getId();
    }

}
