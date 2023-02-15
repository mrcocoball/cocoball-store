package com.dateplanner.place.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "placeId"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private Category category;

    private String placeName;

    private String placeId;

    private String placeUrl;

    private String addressName;

    @Column(name = "road_address_name")
    private String roadAddressName;

    @Column(name = "region_1depth_name")
    private String region1DepthName;

    @Column(name = "region_2depth_name")
    private String region2DepthName;

    @Column(name = "region_3depth_name")
    private String region3DepthName;

    private double longitude;

    private double latitude;

    private Long reviewScore;

    private Long reviewCount;

    private String imageUrl;

    private String description;

    private Place(Category category, String placeName, String placeId, String placeUrl, String addressName,
                  String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName,
                  double longitude, double latitude, Long reviewScore, Long reviewCount, String imageUrl, String description) {

        this.category = category;
        this.placeName = placeName;
        this.placeId = placeId;
        this.placeUrl = placeUrl;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reviewScore = reviewScore;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.description = description;

    }

    private Place(Category category, String placeName, String placeId, String placeUrl, String addressName,
                  String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName,
                  double longitude, double latitude, Long reviewScore, Long reviewCount) {

        this.category = category;
        this.placeName = placeName;
        this.placeId = placeId;
        this.placeUrl = placeUrl;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reviewScore = reviewScore;
        this.reviewCount = reviewCount;

    }

    public static Place of(Category category, String placeName, String placeId, String placeUrl, String addressName,
                           String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName,
                           double longitude, double latitude, Long reviewScore, Long reviewCount, String imageUrl, String description) {

        return new Place(category, placeName, placeId, placeUrl, addressName, roadAddressName,
                region1DepthName, region2DepthName, region3DepthName, longitude, latitude, reviewScore, reviewCount, imageUrl, description);
    }

    public static Place of(Category category, String placeName, String placeId, String placeUrl, String addressName,
                           String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName,
                           double longitude, double latitude, Long reviewScore, Long reviewCount) {

        return new Place(category, placeName, placeId, placeUrl, addressName, roadAddressName,
                region1DepthName, region2DepthName, region3DepthName, longitude, latitude, reviewScore, reviewCount);
    }

    public void addScoreAndCount(Long score) {
        this.reviewCount += 1;
        this.reviewScore += score;
    }

    public void subtractScoreAndCount(Long score) {
        this.reviewCount -= 1;
        this.reviewScore -= score;
    }

    public void addImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addDescription(String description) {
        this.description = description;
    }

}
