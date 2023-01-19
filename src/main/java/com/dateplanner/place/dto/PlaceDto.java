package com.dateplanner.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {

    private Long id;

    @JsonProperty("category_group_id")
    private String categoryGroupId;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("region_1depth_name")
    private String region1DepthName;

    @JsonProperty("region_2depth_name")
    private String region2DepthName;

    @JsonProperty("region_3depth_name")
    private String region3DepthName;

    @JsonProperty("x")
    private String longitude;

    @JsonProperty("y")
    private String latitude;

    private Long reviewScore;
    private Long reviewCount;

}
