package com.dateplanner.plan.dto;

import com.dateplanner.plan.entity.Plan;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlanDto {

    private Long id;

    private String uid;

    private String title;

    private List<DetailPlanDto> detailPlans;

    public static PlanDto from(Plan entity) {
        return PlanDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .title(entity.getTitle())
                .detailPlans(entity.getDetailPlans()
                        .stream()
                        .map(DetailPlanDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
