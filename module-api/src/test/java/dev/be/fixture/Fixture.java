package dev.be.fixture;

import dev.be.moduleapi.kakao.dto.DocumentDto;
import dev.be.moduleapi.plan.dto.DetailPlanRequestDto;
import dev.be.moduleapi.plan.dto.PlanRequestDto;
import dev.be.moduleapi.review.dto.ReviewRequestDto;
import dev.be.modulecore.domain.bookmark.Bookmark;
import dev.be.modulecore.domain.place.Category;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.domain.review.Review;
import dev.be.modulecore.domain.user.User;

import java.util.Collections;

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
                .uid(1L)
                .password("test")
                .email("test")
                .nickname("test")
                .social(false)
                .deleted(false)
                .build();
    }

    // 테스트용 장소 (고정)
    public static Place place() {
        return Place.of(
                category(),
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
        return Place.of(
                category(),
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
    public static Review review() {
        return Review.of(
                user(),
                place(),
                "1",
                "test",
                "test",
                0L
        );
    }

    // 테스트용 리뷰 (업데이트 리뷰)
    public static Review updateReview(String title, String description, Long reviewScore) {
        return Review.of(
                user(),
                place(),
                "1",
                title,
                description,
                reviewScore
        );
    }

    // 테스트용 북마크 (고정)
    public static Bookmark bookmark() {
        return Bookmark.of(
                user(),
                place(),
                "1"
        );
    }

    // 테스트용 북마크 (동적)
    public static Bookmark bookmark(User user, Place place, String kpid) {
        return Bookmark.of(
                user,
                place,
                kpid);
    }

    // 테스트용 플랜 (고정)
    public static Plan plan() {
        return Plan.of(
                user(),
                "test",
                false,
                null
        );
    }

    // 테스트용 플랜 (업데이트 플랜)
    public static Plan updatePlan(String title, boolean finished, String comment) {
        return Plan.of(
                user(),
                title,
                finished,
                comment
        );
    }

    // 테스트용 세부 플랜 (고정)
    public static DetailPlan detailPlan() {
        return DetailPlan.of(
                plan(),
                place(),
                "1",
                1
        );
    }

    // 테스트용 세부 플랜 (업데이트 세부 플랜)
    public static DetailPlan updateDetailPlan(Place place, String kpid, int ord) {
        return DetailPlan.of(
                plan(),
                place,
                kpid,
                ord
        );
    }

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

    // 테스트용 장소 (고정)

    // 테스트용 장소 (동적)

    // 테스트용 리뷰 (고정)
    public static ReviewRequestDto reviewRequestDto() {
        return ReviewRequestDto.of(
                1L,
                "test",
                1L,
                "1",
                "test",
                "test",
                0L,
                Collections.emptyList()
        );
    }

    public static ReviewRequestDto reviewUpdateRequestDto() {
        return ReviewRequestDto.of(
                1L,
                "test",
                1L,
                "1",
                "test-update",
                "test-update",
                5L,
                Collections.emptyList()
        );
    }

    // 테스트용 리뷰 (동적)

    // 테스트용 북마크 (고정)

    // 테스트용 북마크 (동적)

    // 테스트용 플랜 (고정)
    public static PlanRequestDto planRequestDto() {
        return PlanRequestDto.of(
                1L,
                "test",
                "test"
        );
    }

    public static PlanRequestDto planUpdateRequestDto() {
        return PlanRequestDto.of(
                1L,
                "test",
                "test-update"
        );
    }

    // 테스트용 플랜 (동적)

    // 테스트용 세부 플랜 (고정)
    public static DetailPlanRequestDto detailPlanRequestDto() {
        return DetailPlanRequestDto.of(
                1L,
                1,
                1L,
                "1"
        );
    }

    public static DetailPlanRequestDto detailPlanUpdateRequestDto() {
        return DetailPlanRequestDto.of(
                1L,
                2,
                2L,
                "2"
        );
    }

    // 테스트용 세부 플랜 (동적)

}
