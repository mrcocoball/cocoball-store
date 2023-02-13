package com.dateplanner.plan.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.plan.dto.DetailPlanRequestDto;
import com.dateplanner.plan.entity.DetailPlan;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.plan.repository.DetailPlanRepository;
import com.dateplanner.plan.repository.PlanRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 세부 플랜 서비스 - 세부 플랜 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class DetailPlanServiceTest {

    @InjectMocks
    private DetailPlanService sut;

    @Mock
    private DetailPlanRepository detailPlanRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlaceRepository placeRepository;


    @DisplayName("CREATE - 세부 플랜 저장")
    @Test
    public void 세부플랜_저장_성공() {

        // Given
        Plan plan = Fixture.plan();
        Place place = Fixture.place();
        DetailPlanRequestDto dto = Fixture.detailPlanRequestDto();
        given(planRepository.findById(dto.getPid())).willReturn(Optional.of(plan));
        given(placeRepository.findByPlaceId(dto.getKpid())).willReturn(Optional.of(place));
        given(detailPlanRepository.save(any(DetailPlan.class))).willReturn(Fixture.detailPlan());

        // When
        sut.saveDetailPlan(dto);

        // Then
        then(planRepository).should().findById(dto.getPid());
        then(placeRepository).should().findByPlaceId(dto.getKpid());
        then(detailPlanRepository).should().save(any(DetailPlan.class));

    }

    @Disabled("단일 테스트로 돌리기 어려울 것 같다...")
    @DisplayName("UPDATE - 세부 플랜 수정")
    @Test
    public void 세부플랜_수정_성공() {

        // Given

        // When

        // Then

    }

    @DisplayName("DELETE - 세부 플랜 삭제")
    @Test
    public void 세부플랜_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(detailPlanRepository).deleteById(id);

        // When
        sut.deleteDetailPlan(id);

        // Then
        then(detailPlanRepository).should().deleteById(id);

    }

}