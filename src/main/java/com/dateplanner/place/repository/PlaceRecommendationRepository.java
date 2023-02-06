package com.dateplanner.place.repository;

import com.dateplanner.place.dto.PlaceRecommendationDto;
import com.dateplanner.place.entity.QCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dateplanner.place.entity.QCategory.category;
import static com.dateplanner.place.entity.QPlace.place;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class PlaceRecommendationRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public PlaceRecommendationRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    // TODO : 현재 리뷰 데이터가 많지 않아서 단순 평점만으로 정렬하기에는 다소 엉성한 감이 있다.
    //  데이터가 많이 쌓일 경우 리뷰 횟수에도 조건을 두어 평점의 신뢰도를 조금이라도 높이는 것이 좋을 듯 하다.
    public List<PlaceRecommendationDto> placeRecommendation(@Param("region1") String region1,
                                                            @Param("region2") String region2,
                                                            @Param("region3") String region3) {

        NumberExpression<Long> avgReviewScorePath = place.reviewScore.divide(place.reviewCount);

        return jpaQueryFactory
                .select(Projections.fields(PlaceRecommendationDto.class,
                        place.id.as("id"),
                        category.id.as("categoryGroupId"),
                        category.categoryName.as("categoryName"),
                        place.placeName.as("placeName"),
                        place.placeId.as("placeId"),
                        place.addressName.as("addressName"),
                        place.roadAddressName.as("roadAddressName"),
                        place.reviewScore.as("reviewScore"),
                        place.reviewCount.as("reviewCount")
                        ))
                .from(place)
                .leftJoin(place.category, category)
                .where(
                        recommendationCondition(region1, region2, region3)
                )
                .orderBy(avgReviewScorePath.desc())
                .limit(50)
                .fetch();
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
    
    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? QCategory.category.id.eq(category) : null;
    }

    private BooleanExpression recommendationCondition(String region1, String region2, String region3) {
        return region1Eq(region1).and(region2Eq(region2)).and(region3Cont(region3));
    }

}
