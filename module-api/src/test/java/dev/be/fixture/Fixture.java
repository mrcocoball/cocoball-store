package dev.be.fixture;

import dev.be.moduleapi.kakao.dto.DocumentDto;
import dev.be.moduleapi.plan.dto.DetailPlanRequestDto;
import dev.be.moduleapi.plan.dto.PlanRequestDto;
import dev.be.moduleapi.review.dto.ReviewRequestDto;
import dev.be.moduleapi.support.dto.AnswerRequestDto;
import dev.be.moduleapi.support.dto.QuestionRequestDto;
import dev.be.modulecore.domain.bookmark.Bookmark;
import dev.be.modulecore.domain.place.Category;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.plan.DetailPlan;
import dev.be.modulecore.domain.plan.Plan;
import dev.be.modulecore.domain.review.Review;
import dev.be.modulecore.domain.support.*;
import dev.be.modulecore.domain.user.User;

import java.util.Collections;

public class Fixture {

    /**
     * 엔티티
     */

    public static Category category() {
        return Category.of("CE7", "카페");
    }

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

    public static Place place() {
        return Place.of(
                category(),
                category().getId(),
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

    public static Place newPlace() {
        return Place.of(
                category(),
                category().getId(),
                "testplace2",
                "2",
                "testurl2",
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

    public static Bookmark bookmark() {
        return Bookmark.of(
                user(),
                place(),
                "1"
        );
    }

    public static Plan plan() {
        return Plan.of(
                user(),
                "test",
                false,
                null
        );
    }

    public static DetailPlan detailplan() {
        return DetailPlan.of(
                plan(),
                place(),
                "1",
                1
        );
    }

    public static AnnouncementCategory announcementCategory() {
        return AnnouncementCategory.of(
                "test"
        );
    }

    public static Announcement announcement() {
        return Announcement.of(
                "test",
                "test",
                announcementCategory()
        );
    }

    public static FavoriteQuestionCategory favoriteQuestionCategory() {
        return FavoriteQuestionCategory.of(
                "test"
        );
    }

    public static FavoriteAnswer favoriteAnswer() {
        return FavoriteAnswer.of(
                "test",
                "test",
                favoriteQuestionCategory()
        );
    }

    public static QuestionCategory questionCategory() {
        return QuestionCategory.of(
                "test"
        );
    }

    public static Question question() {
        return Question.of(
                "test",
                "test",
                user(),
                questionCategory()
        );
    }

    public static Answer answer() {
        return Answer.of(
                user(),
                question(),
                "test"
        );
    }


    /**
     * DTO
     */

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

    public static ReviewRequestDto reviewRequestDto() {
        return ReviewRequestDto.builder()
                .id(1L)
                .nickname("test")
                .pid(1L)
                .placeId("1")
                .title("test")
                .description("test")
                .reviewScore(0L)
                .fileNames(Collections.emptyList())
                .build();
    }

    public static ReviewRequestDto reviewUpdateRequestDto() {
        return ReviewRequestDto.builder()
                .id(1L)
                .nickname("test")
                .pid(1L)
                .placeId("1")
                .title("test-update")
                .description("test-update")
                .reviewScore(5L)
                .fileNames(Collections.emptyList())
                .build();
    }

    public static PlanRequestDto planRequestDto() {
        return PlanRequestDto.builder()
                .id(1L)
                .nickname("test")
                .title("test")
                .build();
    }

    public static PlanRequestDto planUpdateRequestDto() {
        return PlanRequestDto.builder()
                .id(1L)
                .nickname("test")
                .title("test-update")
                .build();
    }

    public static DetailPlanRequestDto detailPlanRequestDto() {
        return DetailPlanRequestDto.builder()
                .id(1L)
                .ord(1)
                .pid(1L)
                .kpid("1")
                .build();
    }

    public static DetailPlanRequestDto detailPlanUpdateRequestDto() {
        return DetailPlanRequestDto.builder()
                .id(1L)
                .ord(2)
                .pid(2L)
                .kpid("2")
                .build();
    }

    public static QuestionRequestDto questionRequestDto() {
        return QuestionRequestDto.builder()
                .id(1L)
                .title("test")
                .description("test")
                .categoryId(1L)
                .nickname("test")
                .build();
    }

    public static QuestionRequestDto questionUpdateRequestDto() {
        return QuestionRequestDto.builder()
                .id(1L)
                .title("test-update")
                .description("test-update")
                .categoryId(1L)
                .nickname("test")
                .build();
    }

    public static AnswerRequestDto answerRequestDto() {
        return AnswerRequestDto.builder()
                .id(1L)
                .nickname("test")
                .qid(1L)
                .description("test")
                .build();
    }

}
