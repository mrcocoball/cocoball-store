package dev.be.moduleapi.plan.dto;

import dev.be.modulecore.domain.plan.DetailPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetailPlanDto {

    @Schema(description = "세부 플랜(목적지) ID")
    private Long id;

    @Schema(description = "세부 플랜(목적지) 순서")
    private int ord;

    @Schema(description = "세부 플랜(목적지)가 속해 있는 플랜 ID")
    private Long pid;

    @Schema(description = "목적지 장소의 place_id")
    private String kpid;

    @Schema(description = "목적지 장소명")
    private String placeName;

    @Schema(description = "목적지 장소의 지번 주소")
    private String addressName;

    @Schema(description = "목적지 장소의 경도(x)")
    private double longitude;

    @Schema(description = "목적지 장소의 위도(y)")
    private double latitude;

    public static DetailPlanDto from(DetailPlan entity) {
        return DetailPlanDto.builder()
                .id(entity.getId())
                .ord(entity.getOrd())
                .pid(entity.getPlace().getId())
                .kpid(entity.getKpid())
                .placeName(entity.getPlace().getPlaceName())
                .addressName(entity.getPlace().getAddressName())
                .longitude(entity.getPlace().getLongitude())
                .latitude(entity.getPlace().getLatitude())
                .build();
    }

}
