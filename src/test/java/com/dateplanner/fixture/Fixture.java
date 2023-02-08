package com.dateplanner.fixture;

import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.place.entity.Category;
import com.dateplanner.place.entity.Place;
import com.dateplanner.user.entity.User;

public class Fixture {

    /**
     * 엔티티
     */

    // 테스트용 카테고리 (고정)
    public static Category category() {
        return Category.of("CE7", "카페");
    }

    // 테스트용 유저 (고정)
    public static User user() {
        return User.builder()
                .uid("test")
                .password("test")
                .email("test")
                .introduce("test")
                .social(false)
                .deleted(false)
                .build();
    }

    // 테스트용 장소 (고정)
    public static Place place() {
        return Place.of(category(),
                "testplace",
                "1",
                "testurl",
                "서울 관악구 봉천동",
                "서울 관악구 봉천동",
                "서울",
                "관악구",
                "봉천동",
                126.94,
                37.47,
                0L,
                0L
                );
    }

    // 테스트용 장소 (동적)
    public static Place place(String placeName, String placeId, String placeUrl, String addressName, String roadAddressName,
                              String region1DepthName, String region2DepthName, String region3DepthName,
                              double longitude, double latitude, Long reviewScore, Long reviewCount) {
        return Place.of(category(),
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
                reviewScore,
                reviewCount);
    }

    // 테스트용 리뷰 (고정)

    // 테스트용 리뷰 (동적)

    // 테스트용 북마크 (고정)

    // 테스트용 북마크 (동적)

    // 테스트용 플랜 (고정)

    // 테스트용 플랜 (동적)

    // 테스트용 세부 플랜 (고정)

    // 테스트용 세부 플랜 (동적)


    /**
     * DTO
     */

    // 카카오 API Response 내의 DocumentDto (고정)
    public static DocumentDto documentDto() {
        return DocumentDto.builder()
                .categoryGroupId("CE7")
                .placeName("test")
                .placeId("1")
                .placeUrl("test")
                .addressName("서울 관악구 봉천동")
                .roadAddressName("서울 관악구 봉천동")
                .region1DepthName("서울")
                .region2DepthName("관악구")
                .region3DepthName("봉천동")
                .longitude(126.94)
                .latitude(37.47)
                .build();
    }

}
