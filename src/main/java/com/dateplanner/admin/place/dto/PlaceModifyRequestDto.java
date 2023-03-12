package com.dateplanner.admin.place.dto;

import com.dateplanner.place.entity.Place;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceModifyRequestDto {

    private Long id;

    private String categoryGroupId;

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

    private String imageUrl;

    private String description;

    public static PlaceModifyRequestDto from(Place entity) {
        return PlaceModifyRequestDto.builder()
                .id(entity.getId())
                .categoryGroupId(entity.getCategory().getId())
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
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .build();
    }

}
