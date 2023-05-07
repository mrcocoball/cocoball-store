package dev.be.moduleapi.plan.service;

import dev.be.moduleapi.advice.exception.DetailPlanNotFoundApiException;
import dev.be.moduleapi.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleapi.advice.exception.PlanNotFoundApiException;
import dev.be.moduleapi.plan.dto.DetailPlanDto;
import dev.be.moduleapi.plan.dto.DetailPlanRequestDto;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.repositories.place.PlaceRepository;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import dev.be.modulecore.repositories.plan.PlanRepository;
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


    public DetailPlanDto saveDetailPlan(DetailPlanRequestDto dto) {

        Plan plan = planRepository.findById(dto.getPid()).orElseThrow(PlanNotFoundApiException::new);
        Place place = placeRepository.findByPlaceId(dto.getKpid()).orElseThrow(PlaceNotFoundApiException::new);
        DetailPlan detailPlan = detailPlanRepository.save(dto.toEntity(plan, place, dto.getKpid(), dto.getOrd()));
        plan.addDetailPlans(detailPlan);

        return DetailPlanDto.from(detailPlan);
    }

    public DetailPlanDto updateDetailPlan(DetailPlanRequestDto dto) {

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

        return DetailPlanDto.from(detailPlan);
    }

    public void deleteDetailPlan(Long id) {
        detailPlanRepository.deleteById(id);
    }

}
