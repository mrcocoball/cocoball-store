package com.dateplanner.review.dto;

import com.dateplanner.place.entity.Place;
import com.dateplanner.review.entity.Review;
import com.dateplanner.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

    // 첨부 파일 주소
    private List<String> fileNames;

    private ReviewRequestDto(Long id, String uid, Long pid, String placeId, String title, String description, Long reviewScore, List<String> fileNames) {
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
        this.fileNames = fileNames;
    }

    public static ReviewRequestDto of(Long id, String uid, Long pid, String placeId, String title, String description, Long reviewScore, List<String> fileNames) {
        return new ReviewRequestDto(id, uid, pid, placeId, title, description, reviewScore, fileNames);
    }

    public static ReviewRequestDto from(Review entity) {

        // 리뷰 내의 파일명 가져오기
        List<String> fileNames = entity.getImages()
                .stream()
                .sorted()
                .map(image -> image.getUuid() + "_" + image.getFileName())
                .collect(Collectors.toList());

        return new ReviewRequestDto(
                entity.getId(),
                entity.getUser().getUid(),
                entity.getPlace().getId(),
                entity.getPlace().getPlaceId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getReviewScore(),
                fileNames
        );
    }

    public Review toEntity(User user, Place place, String title, String description, Long reviewScore) {

        Review review = Review.of(
                user,
                place,
                place.getPlaceId(),
                title,
                description,
                reviewScore
        );

        List<String> fileNames = this.fileNames;

        if (fileNames != null) {
            fileNames.forEach(fileName -> {
                String[] arr = fileName.split("_", 2);
                review.addImage(arr[0], arr[1]);
            });
        }

        return review;
    }

}
