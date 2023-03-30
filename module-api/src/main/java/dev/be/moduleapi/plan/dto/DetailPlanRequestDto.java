package dev.be.moduleapi.plan.dto;

import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.domain.plan.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailPlanRequestDto {

    @Schema(description = "세부 플랜(목적지) ID, 세부 플랜 수정 시에만 사용")
    private Long id;

    @Schema(description = "세부 플랜(목적지) 순서")
    private int ord;

    @Schema(description = "세부 플랜(목적지)가 속해 있는 플랜 ID")
    private Long pid;

    @Schema(description = "목적지 장소의 place_id")
    private String kpid;

    private DetailPlanRequestDto(Long id, int ord, Long pid, String kpid) {
        this.id = id;
        this.ord = ord;
        this.pid = pid;
        this.kpid = kpid;
    }

    public static DetailPlanRequestDto of(Long id, int ord, Long pid, String kpid) {
        return new DetailPlanRequestDto(id, ord, pid, kpid);
    }

    public DetailPlan toEntity(Plan plan, Place place, String kpid, int ord) {
        return DetailPlan.of(
                plan,
                place,
                kpid,
                ord
        );
    }

}
