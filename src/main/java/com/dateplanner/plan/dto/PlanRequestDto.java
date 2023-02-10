package com.dateplanner.plan.dto;

import com.dateplanner.plan.entity.Plan;
import com.dateplanner.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanRequestDto {

    @Schema(description = "플랜 ID, 플랜 수정 시에만 사용")
    private Long id;

    @Schema(description = "플랜 작성자 ID, 플랜 수정 시에만 사용")
    private String uid;

    @Schema(description = "플랜 제목, 무조건 기입되어야 함")
    @NotEmpty
    @NotNull
    private String title;

    @Schema(description = "코멘트, 플랜이 '완료된 상태' 에서만 작성 / 수정할 수 있어야 함")
    private String comment;

    private PlanRequestDto(Long id, String uid, String title) {
        this.id = id;
        this.uid = uid;
        this.title = title;
    }

    public static PlanRequestDto of(Long id, String uid, String title) {
        return new PlanRequestDto(id, uid, title);
    }

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
