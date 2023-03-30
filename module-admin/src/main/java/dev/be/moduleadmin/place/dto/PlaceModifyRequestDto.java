package dev.be.moduleadmin.place.dto;

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

    public static PlaceModifyRequestDto from(PlaceAdminDetailDto dto) {
        return PlaceModifyRequestDto.builder()
                .id(dto.getId())
                .categoryGroupId(dto.getCategoryGroupId())
                .placeName(dto.getPlaceName())
                .placeId(dto.getPlaceId())
                .placeUrl(dto.getPlaceUrl())
                .addressName(dto.getAddressName())
                .roadAddressName(dto.getRoadAddressName())
                .region1DepthName(dto.getRegion1DepthName())
                .region2DepthName(dto.getRegion2DepthName())
                .region3DepthName(dto.getRegion3DepthName())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .build();
    }

}
