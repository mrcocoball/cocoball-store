package com.dateplanner.review.dto;

import com.dateplanner.place.entity.Place;
import com.dateplanner.review.entity.Review;
import com.dateplanner.admin.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "리뷰 ID, 리뷰 수정 시에만 사용")
    private Long id;

    @Schema(description = "리뷰 작성자 닉네임, 리뷰 수정 시에만 사용")
    private String nickname;

    @Schema(description = "리뷰 대상 장소의 ID (DB PK), 리뷰 수정 시에만 사용")
    private Long pid;

    @Schema(description = "리뷰 대상 장소의 place_id (String)")
    private String placeId;

    @Schema(description = "리뷰 제목, 무조건 기입되어야 함")
    @NotEmpty
    @NotNull
    private String title;

    @Schema(description = "리뷰 내용, 무조건 기입되어야 함")
    @NotEmpty
    @NotNull
    private String description;

    @Schema(description = "장소에 대한 평점 (리뷰 평점), 0~5까지만")
    @Max(5)
    private Long reviewScore;

    // 첨부 파일 주소
    @Schema(description = "첨부 이미지 파일 주소 리스트")
    private List<String> fileNames;

    private ReviewRequestDto(Long id, String nickname, Long pid, String placeId, String title, String description, Long reviewScore, List<String> fileNames) {
        this.id = id;
        this.nickname = nickname;
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

    public static ReviewRequestDto of(Long id, String nickname, Long pid, String placeId, String title, String description, Long reviewScore, List<String> fileNames) {
        return new ReviewRequestDto(id, nickname, pid, placeId, title, description, reviewScore, fileNames);
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
                entity.getUser().getNickname(),
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
