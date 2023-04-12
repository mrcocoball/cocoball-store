package dev.be.moduleapi.plan.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.advice.exception.DetailPlanNotFoundApiException;
import dev.be.moduleapi.plan.dto.DetailPlanDto;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[단일] 플랜 화면 처리 서비스 - 세부 플랜 조회 테스트")
@ExtendWith(MockitoExtension.class)
class DetailPlanApiServiceTest {

    @InjectMocks
    private DetailPlanApiService sut;

    @Mock
    private DetailPlanRepository detailPlanRepository;


    @DisplayName("READ - 세부 플랜 단건 조회")
    @Test
    public void 세부플랜_단건_조회_성공() {

        // Given
        Long id = 1L;
        DetailPlan detailPlan = Fixture.detailplan();
        String nickname = detailPlan.getPlan().getUser().getNickname();
        given(detailPlanRepository.findById(id)).willReturn(Optional.of(detailPlan));

        // When
        DetailPlanDto dto = sut.getDetailPlan(id, nickname);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", detailPlan.getId())
                .hasFieldOrPropertyWithValue("kpid", detailPlan.getKpid());
        then(detailPlanRepository).should(BDDMockito.times(2)).findById(id);

    }

    @DisplayName("READ - 세부 플랜 단건 조회 - 실패(존재하지 않는 세부 플랜)")
    @Test
    public void 세부플랜_단건_조회_실패() {

        // Given
        Long id = 1L;
        String nickname = "test";
        given(detailPlanRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(DetailPlanNotFoundApiException.class, () -> {
            sut.getDetailPlan(id, nickname);
        });
        then(detailPlanRepository).should().findById(id);

    }

    @DisplayName("READ - 세부 플랜 단건 조회 - 실패(접근 권한이 없는 경우)")
    @Test
    public void 세부플랜_단건_조회_실패_접근권한없음() {

        // Given
        Long id = 1L;
        DetailPlan detailPlan = Fixture.detailplan();
        String nickname = "INVALID";
        given(detailPlanRepository.findById(id)).willReturn(Optional.of(detailPlan));

        // When & Then
        assertThrows(AccessDeniedException.class, () -> {
            sut.getDetailPlan(id, nickname);
        });
        then(detailPlanRepository).should().findById(id);

    }

}