package com.dateplanner.admin.place.dto;

import com.dateplanner.place.entity.Category;
import com.dateplanner.place.entity.Place;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRequestDto {

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


    public Place toEntity(String categoryGroupId, String placeName, String placeId, String placeUrl, String addressName,
                          String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName,
                          double longitude, double latitude, String imageUrl, String description) {
        return Place.of(
                Category.of(categoryGroupId),
                placeName,
                placeId,
                placeUrl,
                addressName,
                roadAddressName,
                region1DepthName,
                region2DepthName,
                region3DepthName,
                longitude,
                latitude,
                0L,
                0L,
                imageUrl,
                description
        );
    }

}
