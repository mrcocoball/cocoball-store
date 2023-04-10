package dev.be.moduleapi.plan.dto;

import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlanRequestDto {

    @Schema(description = "플랜 ID, 플랜 수정 시에만 사용")
    private Long id;

    @Schema(description = "플랜 작성자 닉네임, 플랜 수정 시에만 사용")
    private String nickname;

    @Schema(description = "플랜 제목, 무조건 기입되어야 함")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    @NotNull(message = "제목이 입력되어야 합니다")
    private String title;

    @Schema(description = "코멘트, 플랜이 '완료된 상태' 에서만 작성 / 수정할 수 있어야 함")
    private String comment;

    public Plan toEntity(User user, String title) {
        return Plan.of(
                user,
                title
        );
    }

    public Plan toEntity(User user, String title, boolean finished, String comment) {
        return Plan.of(
                user,
                title,
                finished,
                comment
        );
    }
}
