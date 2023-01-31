package com.dateplanner.plan.dto;

import com.dateplanner.place.entity.Place;
import com.dateplanner.plan.entity.DetailPlan;
import com.dateplanner.plan.entity.Plan;
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

    private Long id;

    private int ord;

    private Long pid;

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
