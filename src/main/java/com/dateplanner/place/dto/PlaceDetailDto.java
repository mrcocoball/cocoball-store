package com.dateplanner.place.dto;

import com.dateplanner.place.entity.Place;
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
public class PlaceDetailDto {

    private Long id;

    @JsonProperty("category_group_id")
    private String categoryGroupId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("region_1depth_name")
    private String region1DepthName;

    @JsonProperty("region_2depth_name")
    private String region2DepthName;

    @JsonProperty("region_3depth_name")
    private String region3DepthName;

    @JsonProperty("x")
    private double longitude;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("review_score")
    private Long reviewScore;

    @JsonProperty("review_count")
    private Long reviewCount;

    @JsonProperty("avg_review_score")
    private double avgReviewScore;

    @JsonProperty("bookmarked")
    private boolean bookmarked;


    public static PlaceDetailDto from(Place entity, boolean isBookmarked) {

        return PlaceDetailDto.builder()
                .id(entity.getId())
                .categoryGroupId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getCategoryName())
                .placeName(entity.getPlaceName())
                .placeId(entity.getPlaceId())
                .placeUrl(entity.getPlaceUrl())
                .addressName(entity.getAddressName())
                .roadAddressName(entity.getRoadAddressName())
                .region1DepthName(entity.getRegion1DepthName())
                .region2DepthName(entity.getRegion2DepthName())
                .region3DepthName(entity.getRegion3DepthName())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .reviewScore(entity.getReviewScore())
                .reviewCount(entity.getReviewCount())
                .avgReviewScore(calculateAvgScore(entity.getReviewScore(), entity.getReviewCount()))
                .bookmarked(isBookmarked)
                .build();
    }

    public static double calculateAvgScore(Long reviewScore, Long reviewCount) {

        if (reviewScore == null || reviewScore == 0L) {
            return 0;
        }

        if (reviewCount == null || reviewCount == 0L) {
            return 0;
        }

        return (double) reviewScore / reviewCount;
    }

}
