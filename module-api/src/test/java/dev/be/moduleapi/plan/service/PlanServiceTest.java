package dev.be.moduleapi.plan.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.plan.dto.PlanRequestDto;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.plan.DetailPlanRepository;
import dev.be.modulecore.repositories.plan.PlanRepository;
import dev.be.modulecore.repositories.user.UserRepository;
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

@DisplayName("[단일] 플랜 서비스 - 플랜 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @InjectMocks
    private PlanService sut;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DetailPlanRepository detailPlanRepository;


    @DisplayName("CREATE - 플랜 저장")
    @Test
    public void 플랜_저장_성공() {

        // Given
        User user = Fixture.user();
        PlanRequestDto dto = Fixture.planRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(planRepository.save(any(Plan.class))).willReturn(Fixture.plan());

        // When
        sut.savePlan(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getNickname());
        then(planRepository).should().save(any(Plan.class));

    }

    @Disabled("단일 테스트로 돌리기 어려울 것 같다...")
    @DisplayName("UPDATE - 플랜 수정")
    @Test
    public void 플랜_수정_성공() {

        // Given

        // When

        // Then

    }

    @DisplayName("DELETE - 플랜 삭제")
    @Test
    public void 플랜_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(planRepository).deleteById(id);

        // When
        sut.deletePlan(id);

        // Then
        then(planRepository).should().deleteById(id);

    }

    @Disabled("단일 테스트로 돌리기 어려울 것 같다...")
    @DisplayName("UPDATE - 플랜 종료 처리")
    @Test
    public void 플랜_종료_성공() {

        // Given

        // When

        // Then

    }

}