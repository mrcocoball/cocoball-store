package com.dateplanner.review.dto;

import com.dateplanner.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long id;

    private String uid;

    private Long pid;

    private String placeId;

    private String placeName;

    private String placeUrl;

    private String title;

    private String description;

    private Long reviewScore;

    // 첨부파일 이름 리스트
    private List<String> fileNames;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    public static ReviewDto from(Review entity) {

        List<String> fileNames = entity.getImages()
                .stream()
                .sorted()
                .map(image -> image.getUuid() + "_" + image.getFileName())
                .collect(Collectors.toList());

        return ReviewDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .pid(entity.getPlace().getId())
                .placeId(entity.getPlace().getPlaceId())
                .placeName(entity.getPlace().getPlaceName())
                .placeUrl(entity.getPlace().getPlaceUrl())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .reviewScore(entity.getReviewScore())
                .fileNames(fileNames)
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
