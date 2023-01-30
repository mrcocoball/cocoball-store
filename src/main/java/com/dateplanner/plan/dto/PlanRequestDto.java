package com.dateplanner.plan.dto;

import com.dateplanner.place.entity.Place;
import com.dateplanner.plan.entity.Plan;
import com.dateplanner.review.entity.Review;
import com.dateplanner.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
