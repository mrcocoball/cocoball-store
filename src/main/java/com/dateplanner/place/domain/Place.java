package com.dateplanner.place.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String placeName;

    private String placeId;

    private String placeUrl;

    private String addressName;

    private String region_1depth_name;

    private String region_2depth_name;

    private String region_3depth_name;

    private String longitude;

    private String latitude;

    private Long reviewScore;

    private Long reviewCount;

    private Place(Category category, String placeName, String placeId, String addressName, String region_1depth_name,
                  String region_2depth_name, String region_3depth_name, String longitude, String latitude, Long reviewCount, Long reviewScore) {
        this.category = category;
        this.placeName = placeName;
        this.placeId = placeId;
        this.addressName = addressName;
        this.region_1depth_name = region_1depth_name;
        this.region_2depth_name = region_2depth_name;
        this.region_3depth_name = region_3depth_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
    }

    public void addScoreAndCount(Long score) {
        this.reviewCount += 1;
        this.reviewScore += score;
    }

    public void subtractScoreAndCount(Long score) {
        this.reviewCount -= 1;
        this.reviewScore -= score;
    }

}
