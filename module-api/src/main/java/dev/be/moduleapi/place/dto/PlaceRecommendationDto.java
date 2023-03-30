package dev.be.moduleapi.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRecommendationDto {

    @Schema(description = "장소 ID (DB상 PK)")
    private Long id;

    @Schema(description = "카테고리 ID")
    @JsonProperty("category_group_id")
    private String categoryGroupId;

    @Schema(description = "카테고리명")
    @JsonProperty("category_name")
    private String categoryName;

    @Schema(description = "장소명")
    @JsonProperty("place_name")
    private String placeName;

    @Schema(description = "장소 ID (카카오, 대부분 해당 ID 사용)")
    @JsonProperty("place_id")
    private String placeId;

    @Schema(description = "장소 지번 주소")
    @JsonProperty("address_name")
    private String addressName;

    @Schema(description = "장소 도로명 주소")
    @JsonProperty("road_address_name")
    private String roadAddressName;

    @Schema(description = "리뷰 총점")
    @JsonProperty("review_score")
    private Long reviewScore;

    @Schema(description = "리뷰 수")
    @JsonProperty("review_count")
    private Long reviewCount;

    @Schema(description = "리뷰 평점")
    @JsonProperty("avg_review_score")
    private double avgReviewScore;

    @Schema(description = "장소 이미지 URL")
    @JsonProperty("image_url")
    private String imageUrl;


    public static double calculateAvgScore(Long reviewScore, Long reviewCount) {

        if (reviewScore == null || reviewScore == 0L) {
            return 0;
        }

        if (reviewCount == null || reviewCount == 0L) {
            return 0;
        }

        return (double) reviewScore / reviewCount;
    }

    public void setAvgReviewScore(double avgReviewScore) {
        this.avgReviewScore = avgReviewScore;
    }

}
