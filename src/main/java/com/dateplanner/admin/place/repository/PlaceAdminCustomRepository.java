package com.dateplanner.admin.place.repository;

import com.dateplanner.admin.place.dto.PlaceAdminDetailDto;
import com.dateplanner.place.entity.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.dateplanner.place.entity.QCategory.category;
import static com.dateplanner.place.entity.QPlace.place;
import static org.springframework.util.StringUtils.hasText;

@Repository
@Slf4j
public class PlaceAdminCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceAdminCustomRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<PlaceAdminDetailDto> placeList(@Param("region1") String region1, @Param("region2") String region2, @Param("region3") String region3,
                                               @Param("categoryId") String categoryId, @Param("id") Long id, @Param("placeId") String placeId,
                                               @Param("placeName") String placeName, @Param("reviewCount") Long reviewCount,
                                               @Param("startDate") LocalDate startDate, @Param("targetDate") LocalDate targetDate) {

        List<PlaceAdminDetailDto> dtos = jpaQueryFactory
                .select(Projections.fields(PlaceAdminDetailDto.class,
                        place.id.as("id"),
                        category.id.as("categoryGroupId"),
                        category.categoryName.as("categoryName"),
                        place.placeName.as("placeName"),
                        place.placeId.as("placeId"),
                        place.placeUrl.as("placeUrl"),
                        place.addressName.as("addressName"),
                        place.roadAddressName.as("roadAddressName"),
                        place.region1DepthName.as("region1DepthName"),
                        place.region2DepthName.as("region2DepthName"),
                        place.region3DepthName.as("region3DepthName"),
                        place.latitude.as("latitude"),
                        place.longitude.as("longitude"),
                        place.reviewScore.as("reviewScore"),
                        place.reviewCount.as("reviewCount"),
                        place.imageUrl.as("imageUrl"),
                        place.description.as("description"),
                        place.createdAt.as("createdAt"),
                        place.modifiedAt.as("modifiedAt")
                ))
                .from(place)
                .leftJoin(place.category, category)
                .where(
                        // TODO : region condtion을 and()로 묶었으나 첫번째 조건이 null일 경우 NPE가 발생하여 분리해두었음. 해당 이슈 개선 방안이 있는지 확인
                        region1Eq(region1),
                        region2Eq(region2),
                        region3Cont(region3),
                        categoryEq(categoryId),
                        idEq(id),
                        placeIdEq(placeId),
                        placeNameEq(placeName),
                        reviewCondition(reviewCount),
                        dateCondition(startDate, targetDate)
                )
                .orderBy(place.modifiedAt.desc())
                .fetch();

        for (PlaceAdminDetailDto dto : dtos) {
            double avgScore = PlaceAdminDetailDto.calculateAvgScore(dto.getReviewScore(), dto.getReviewCount());
            dto.setAvgReviewScore(avgScore);
        }

        return dtos;

    }

    private BooleanExpression region1Eq(String region1) {
        return hasText(region1) ? place.region1DepthName.eq(region1) : null;
    }

    private BooleanExpression region2Eq(String region2) {
        return hasText(region2) ? place.region2DepthName.eq(region2) : null;
    }

    private BooleanExpression region3Cont(String region3) {
        return hasText(region3) ? place.region3DepthName.startsWith(region3) : null;
    }

    private BooleanExpression categoryEq(String categoryId) {
        return hasText(categoryId) ? QCategory.category.id.eq(categoryId) : null;
    }

    private BooleanExpression idEq(Long id) {
        return id != null ? place.id.eq(id) : null;
    }

    private BooleanExpression placeIdEq(String placeId) {
        return hasText(placeId) ? place.placeId.eq(placeId) : null;
    }

    private BooleanExpression placeNameEq(String placeName) {
        return hasText(placeName) ? place.placeName.eq(placeName) : null;
    }

    private BooleanExpression reviewCondition(Long reviewCount) {
        return reviewCount != null ? place.reviewCount.goe(reviewCount) : null;
    }

    private BooleanExpression dateCondition(LocalDate startDate, LocalDate targetDate) {

        LocalDate defaultTargetDate = LocalDate.now();
        LocalDate defaultStartDate = defaultTargetDate.minusDays(7);

        if (startDate == null) {
            startDate = defaultStartDate;
        }

        if (targetDate == null) {
            targetDate = defaultTargetDate;
        }

        return place.modifiedAt.between(startDate.atStartOfDay(), targetDate.atTime(LocalTime.MAX));

    }

}
