package dev.be.moduleapi.plan.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.plan.dto.DetailPlanDto;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 플랜 화면 처리 서비스 - 세부 플랜 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DetailPlanApiServiceTest {

    @Autowired
    private DetailPlanApiService sut;

    @MockBean
    private DetailPlanRepository detailPlanRepository;


    @DisplayName("READ - 세부 플랜 단건 조회")
    @Test
    public void 세부플랜_단건_조회_성공() {

        // Given
        Long id = 1L;
        DetailPlan detailPlan = Fixture.detailPlan();
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

}