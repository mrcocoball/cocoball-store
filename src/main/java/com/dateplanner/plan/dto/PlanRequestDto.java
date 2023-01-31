package com.dateplanner.plan.dto;

import com.dateplanner.plan.entity.Plan;
import com.dateplanner.user.entity.User;
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

    private Long id;

    private String uid;

    @NotEmpty
    @NotNull
    private String title;

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
}
