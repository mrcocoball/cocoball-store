package dev.be.moduleapi.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.be.modulecore.domain.place.Place;
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
public class PlaceDto {

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

    @Schema(description = "장소 상세 페이지 URL (카카오)")
    @JsonProperty("place_url")
    private String placeUrl;

    @Schema(description = "장소 지번 주소")
    @JsonProperty("address_name")
    private String addressName;

    @Schema(description = "장소 도로명 주소")
    @JsonProperty("road_address_name")
    private String roadAddressName;

    @Schema(description = "장소 지번 주소의 시/도")
    @JsonProperty("region_1depth_name")
    private String region1DepthName;

    @Schema(description = "장소 지번 주소의 군/구")
    @JsonProperty("region_2depth_name")
    private String region2DepthName;

    @Schema(description = "장소 지번 주소의 읍/면/동")
    @JsonProperty("region_3depth_name")
    private String region3DepthName;

    @Schema(description = "경도")
    @JsonProperty("x")
    private double longitude;

    @Schema(description = "위도")
    @JsonProperty("y")
    private double latitude;

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

    @Schema(description = "장소 설명")
    private String description;

    @Schema(description = "검색 위치와의 거리, KM")
    private double distance;

    public static PlaceDto from(Place entity, boolean isBookmarked) {

        return PlaceDto.builder()
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
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
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

    public void setDistance(double distance) {
        this.distance = Double.parseDouble(String.format("%.2f", distance));
    }

}
