package com.dateplanner.review.dto;

import com.dateplanner.place.entity.Place;
import com.dateplanner.review.entity.Review;
import com.dateplanner.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    private Long id;

    private String uid;

    private Long pid;

    private String placeId;

    @NotEmpty
    @NotNull
    private String title;

    @NotEmpty
    @NotNull
    private String description;

    @Max(5)
    private Long reviewScore;

    private ReviewRequestDto(Long id, String uid, Long pid, String placeId, String title, String description, Long reviewScore) {
        this.id = id;
        this.uid = uid;
        this.pid = pid;
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        if (reviewScore == null) {
            reviewScore = 0L;
        }
        this.reviewScore = reviewScore;
    }

    public static ReviewRequestDto of(Long id, String uid, Long pid, String placeId, String title, String description, Long reviewScore) {
        return new ReviewRequestDto(id, uid, pid, placeId, title, description, reviewScore);
    }

    public static ReviewRequestDto from(Review entity) {
        return new ReviewRequestDto(
                entity.getId(),
                entity.getUser().getUid(),
                entity.getPlace().getId(),
                entity.getPlace().getPlaceId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getReviewScore()
        );
    }

    public Review toEntity(User user, Place place, String title, String description, Long reviewScore) {
        return Review.of(
                user,
                place,
                place.getPlaceId(),
                title,
                description,
                reviewScore
        );
    }

}
