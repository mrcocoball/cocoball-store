package com.dateplanner.review.dto;

import com.dateplanner.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "리뷰 ID")
    private Long id;

    @Schema(description = "리뷰 작성자 닉네임")
    private String nickname;

    @Schema(description = "리뷰 대상 장소의 ID (DB PK)")
    private Long pid;

    @Schema(description = "리뷰 대상 장소의 place_id (String)")
    private String placeId;

    @Schema(description = "리뷰 대상 장소명")
    private String placeName;

    @Schema(description = "리뷰 대상 장소의 상세 페이지 URL")
    private String placeUrl;

    @Schema(description = "리뷰 제목")
    private String title;

    @Schema(description = "리뷰 내용")
    private String description;

    @Schema(description = "장소에 대한 평점 (리뷰 평점)")
    private Long reviewScore;

    // 첨부파일 이름 리스트
    @Schema(description = "리뷰에 첨부된 이미지 파일 주소 리스트")
    private List<String> fileNames;

    @Schema(description = "리뷰 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema(description = "리뷰 수정 시간")
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
                .nickname(entity.getUser().getNickname())
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
