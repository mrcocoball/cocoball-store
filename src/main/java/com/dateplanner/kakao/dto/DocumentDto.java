package com.dateplanner.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {

    @JsonProperty("category_group_code")
    private String categoryGroupId;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("id")
    private String placeId;

    @JsonProperty("place_url")
    private String placeUrl;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("x")
    private double longitude;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("region_1depth_name")
    private String region1DepthName;

    @JsonProperty("region_2depth_name")
    private String region2DepthName;

    @JsonProperty("region_3depth_name")
    private String region3DepthName;

    public void splitAddress(String addressName) {
        String[] arr = addressName.split(" ", 3);
        this.region1DepthName = arr[0];
        this.region2DepthName = arr[1];
        this.region3DepthName = arr[2];
    }

}
