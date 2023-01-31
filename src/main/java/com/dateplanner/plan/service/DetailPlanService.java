package com.dateplanner.plan.service;

import com.dateplanner.advice.exception.DetailPlanNotFoundApiException;
import com.dateplanner.advice.exception.PlaceNotFoundApiException;
import com.dateplanner.advice.exception.PlanNotFoundApiException;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.plan.dto.DetailPlanRequestDto;
import com.dateplanner.plan.entity.DetailPlan;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.plan.repository.DetailPlanRepository;
import com.dateplanner.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;
    private final PlanRepository planRepository;
    private final PlaceRepository placeRepository;


    public Long saveDetailPlan(DetailPlanRequestDto dto) {

        log.info("[DetailPlanService saveDetailPlan] find plan...");

        Plan plan = planRepository.findById(dto.getPid()).orElseThrow(PlanNotFoundApiException::new);

        log.info("[DetailPlanService saveDetailPlan] find place...");

        Place place = placeRepository.findByPlaceId(dto.getKpid()).orElseThrow(PlaceNotFoundApiException::new);

        log.info("[DetailPlanService saveDetailPlan] save detail plan...");

        DetailPlan detailPlan = detailPlanRepository.save(dto.toEntity(plan, place, dto.getKpid(), dto.getOrd()));
        plan.addDetailPlans(detailPlan);

        log.info("[DetailPlanService saveDetailPlan] save detail plan complete");

        return detailPlan.getId();
    }

    public Long updateDetailPlan(DetailPlanRequestDto dto) {

        DetailPlan detailPlan = detailPlanRepository.findById(dto.getId()).orElseThrow(DetailPlanNotFoundApiException::new);

        // 장소 정보 업데이트
        if (!detailPlan.getKpid().equals(dto.getKpid())) {
            detailPlan.changeKpid(dto.getKpid());
            detailPlan.changePlace(placeRepository.findByPlaceId(dto.getKpid()).orElseThrow(PlaceNotFoundApiException::new));
        }

        // 순서 정보 업데이트
        if (detailPlan.getOrd() != dto.getOrd()) {
            detailPlan.changeOrd(dto.getOrd());
        }

        return detailPlan.getId();
    }

    public void deleteDetailPlan(Long id) {
        detailPlanRepository.deleteById(id);
    }

}
