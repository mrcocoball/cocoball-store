package com.dateplanner.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long id;

    @JsonProperty("category_group_id")
    private String categoryGroupId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("review_score")
    private Long reviewScore;

    @JsonProperty("review_count")
    private Long reviewCount;

    @JsonProperty("avg_review_score")
    private double avgReviewScore;


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
