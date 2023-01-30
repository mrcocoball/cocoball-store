package com.dateplanner.plan.dto;

import com.dateplanner.plan.entity.DetailPlan;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetailPlanDto {

    private Long id;

    private int ord;

    private Long pid;

    private String kpid;

    private String placeName;

    private String addressName;

    private double longitude;

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
