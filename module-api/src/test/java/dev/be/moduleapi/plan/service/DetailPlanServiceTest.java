package dev.be.moduleapi.plan.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.advice.exception.DetailPlanNotFoundApiException;
import dev.be.moduleapi.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleapi.plan.dto.DetailPlanRequestDto;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.repositories.place.PlaceRepository;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import dev.be.modulecore.repositories.plan.PlanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        given(detailPlanRepository.save(any(DetailPlan.class))).willReturn(Fixture.detailplan());

        // When
        sut.saveDetailPlan(dto);

        // Then
        then(planRepository).should().findById(dto.getPid());
        then(placeRepository).should().findByPlaceId(dto.getKpid());
        then(detailPlanRepository).should().save(any(DetailPlan.class));

    }

    @DisplayName("UPDATE - 세부 플랜 수정")
    @Test
    public void 세부플랜_수정_성공() {

        // Given
        DetailPlan detailPlan = Fixture.detailplan();
        Place newPlace = Fixture.newPlace();
        DetailPlanRequestDto updateRequestDto = Fixture.detailPlanUpdateRequestDto();
        given(detailPlanRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(detailPlan));
        given(placeRepository.findByPlaceId(updateRequestDto.getKpid())).willReturn(Optional.of(newPlace));

        // When
        sut.updateDetailPlan(updateRequestDto);

        // Then
        assertThat(detailPlan).hasFieldOrPropertyWithValue("kpid", "2");
        assertThat(detailPlan).hasFieldOrPropertyWithValue("ord", 2);
        then(detailPlanRepository.findById(updateRequestDto.getId()));
        then(placeRepository.findByPlaceId(updateRequestDto.getKpid()));

    }

    @DisplayName("UPDATE - 세부 플랜 수정 - 실패(존재하지 않는 세부 플랜")
    @Test
    public void 세부플랜_수정_실패_존재하지_않는_세부플랜() {

        // Given
        DetailPlanRequestDto updateRequestDto = Fixture.detailPlanUpdateRequestDto();
        given(detailPlanRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(DetailPlanNotFoundApiException.class, () -> {
           sut.updateDetailPlan(updateRequestDto);
        });
        then(detailPlanRepository.findById(updateRequestDto.getId()));

    }

    @DisplayName("UPDATE - 세부 플랜 수정 - 실패(존재하지 않는 장소")
    @Test
    public void 세부플랜_수정_실패_존재하지_않는_장소() {

        // Given
        DetailPlan detailPlan = Fixture.detailplan();
        DetailPlanRequestDto updateRequestDto = Fixture.detailPlanUpdateRequestDto();
        given(detailPlanRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(detailPlan));
        given(placeRepository.findByPlaceId(updateRequestDto.getKpid())).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundApiException.class, () -> {
           sut.updateDetailPlan(updateRequestDto);
        });
        then(detailPlanRepository.findById(updateRequestDto.getId()));
        then(placeRepository.findByPlaceId(updateRequestDto.getKpid()));

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