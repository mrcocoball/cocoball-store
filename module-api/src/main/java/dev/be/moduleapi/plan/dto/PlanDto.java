package dev.be.moduleapi.plan.dto;

import dev.be.modulecore.domain.plan.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlanDto {

    @Schema(description = "플랜 ID, 플랜 수정 시에만 사용")
    private Long id;

    @Schema(description = "플랜 작성자 닉네임, 플랜 수정 시에만 사용")
    private String nickname;

    @Schema(description = "플랜 제목")
    private String title;

    @Schema(description = "세부 플랜(목적지) 목록")
    private List<DetailPlanDto> detailPlans;

    @Schema(description = "플랜 완료 여부, true / false")
    private boolean finished;

    @Schema(description = "코멘트")
    private String comment;

    public static PlanDto from(Plan entity) {
        return PlanDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getNickname())
                .title(entity.getTitle())
                .detailPlans(entity.getDetailPlans()
                        .stream()
                        .map(DetailPlanDto::from)
                        .sorted(Comparator.comparing(DetailPlanDto::getOrd))
                        .collect(Collectors.toList()))
                .finished(entity.isFinished())
                .comment(entity.getComment())
                .build();
    }
}
