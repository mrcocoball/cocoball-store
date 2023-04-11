package dev.be.fixture;

import dev.be.moduleadmin.kakao.dto.DocumentDto;
import dev.be.moduleadmin.place.dto.PlaceCrawlingDto;
import dev.be.moduleadmin.place.dto.PlaceModifyRequestDto;
import dev.be.moduleadmin.place.dto.PlaceRequestDto;
import dev.be.moduleadmin.support.dto.*;
import dev.be.modulecore.domain.place.Category;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.support.*;
import dev.be.modulecore.domain.user.User;

import java.util.ArrayList;
import java.util.List;

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

    public static PlaceModifyRequestDto placeModifyRequestDto() {
        return PlaceModifyRequestDto.builder()
                .id(1L)
                .categoryGroupId("CE7")
                .placeName("testplace-update")
                .placeId("1")
                .placeUrl("testurl-update")
                .addressName("서울 관악구 봉천동")
                .roadAddressName("서울 관악구 봉천동")
                .region1DepthName("서울")
                .region2DepthName("관악구")
                .region3DepthName("봉천동")
                .longitude(126.94)
                .latitude(37.47)
                .build();
    }

    public static AnnouncementCategoryRequestDto announcementCategoryRequestDto() {
        return AnnouncementCategoryRequestDto.builder()
                .categoryName("test")
                .build();
    }

    public static AnnouncementCategoryModifyRequestDto announcementCategoryModifyRequestDto() {
        return AnnouncementCategoryModifyRequestDto.builder()
                .id(1L)
                .categoryName("test-update")
                .build();
    }

    public static AnnouncementRequestDto announcementRequestDto() {
        return AnnouncementRequestDto.builder()
                .title("test")
                .description("test")
                .categoryId(1L)
                .build();
    }

    public static AnnouncementModifyRequestDto announcementModifyRequestDto() {
        return AnnouncementModifyRequestDto.builder()
                .id(1L)
                .title("test-update")
                .description("test-update")
                .categoryId(1L)
                .build();
    }

    public static FavoriteQuestionCategoryRequestDto favoriteQuestionCategoryRequestDto() {
        return FavoriteQuestionCategoryRequestDto.builder()
                .categoryName("test")
                .build();
    }

    public static FavoriteQuestionCategoryModifyRequestDto favoriteQuestionCategoryModifyRequestDto() {
        return FavoriteQuestionCategoryModifyRequestDto.builder()
                .id(1L)
                .categoryName("test-update")
                .build();
    }

    public static FavoriteAnswerRequestDto favoriteAnswerRequestDto() {
        return FavoriteAnswerRequestDto.builder()
                .title("test")
                .description("test")
                .categoryId(1L)
                .build();
    }

    public static FavoriteAnswerModifyRequestDto favoriteAnswerModifyRequestDto() {
        return FavoriteAnswerModifyRequestDto.builder()
                .id(1L)
                .title("test")
                .description("test")
                .categoryId(1L)
                .build();
    }

    public static QuestionCategoryRequestDto questionCategoryRequestDto() {
        return QuestionCategoryRequestDto.builder()
                .categoryName("test")
                .build();
    }

    public static QuestionCategoryModifyRequestDto questionCategoryModifyRequestDto() {
        return QuestionCategoryModifyRequestDto.builder()
                .id(1L)
                .categoryName("test-update")
                .build();
    }

    public static QuestionRequestDto questionRequestDto() {
        return QuestionRequestDto.builder()
                .title("test")
                .description("test")
                .categoryId(1L)
                .username("test")
                .build();
    }

    public static QuestionModifyRequestDto questionModifyRequestDto() {
        return QuestionModifyRequestDto.builder()
                .id(1L)
                .title("test-update")
                .description("test-update")
                .categoryId(1L)
                .username("test")
                .build();
    }

    public static AnswerRequestDto answerRequestDto() {
        return AnswerRequestDto.builder()
                .id(1L)
                .username("test")
                .qid(1L)
                .description("test")
                .build();
    }

    public static List<String> crawlingIds() {
        List<String> result = new ArrayList<>();

        result.add("1367638762");

        return result;
    }

    public static PlaceCrawlingDto placeCrawlingDto() {
        return PlaceCrawlingDto.builder()
                .placeId("1367638762")
                .imageUrl("test")
                .build();
    }
    public static List<PlaceCrawlingDto> placeCrawlingDtoList() {
        List<PlaceCrawlingDto> result = new ArrayList<>();

        result.add(placeCrawlingDto());

        return result;
    }

}
