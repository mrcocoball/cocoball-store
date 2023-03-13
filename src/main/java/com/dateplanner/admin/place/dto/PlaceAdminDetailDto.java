package com.dateplanner.admin.place.dto;

import com.dateplanner.place.entity.Place;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceAdminDetailDto {

    private Long id;

    private String categoryGroupId;

    private String categoryName;

    private String placeName;

    private String placeId;

    private String placeUrl;

    private String addressName;

    private String roadAddressName;

    private String region1DepthName;

    private String region2DepthName;

    private String region3DepthName;

    private double longitude;

    private double latitude;

    private Long reviewCount;

    private Long reviewScore;

    @Setter
    private double avgReviewScore;

    private String imageUrl;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;


    public static double calculateAvgScore(Long reviewScore, Long reviewCount) {

        if (reviewScore == null || reviewScore == 0L) {
            return 0;
        }

        if (reviewCount == null || reviewCount == 0L) {
            return 0;
        }

        return (double) reviewScore / reviewCount;
    }

    public static PlaceAdminDetailDto from(Place entity) {
        return PlaceAdminDetailDto.builder()
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
                .reviewCount(entity.getReviewCount())
                .reviewScore(entity.getReviewScore())
                .avgReviewScore(calculateAvgScore(entity.getReviewScore(), entity.getReviewCount()))
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
