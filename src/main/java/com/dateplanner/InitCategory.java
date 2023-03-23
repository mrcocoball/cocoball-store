package com.dateplanner;

import com.dateplanner.admin.consumer.entity.*;
import com.dateplanner.place.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class InitCategory {

    private final InitCategoryService initCategoryService;

    @PostConstruct
    public void init() {
        initCategoryService.init();
    }

    @Component
    static class InitCategoryService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {

            AnnouncementCategory category1 = AnnouncementCategory.of("일반");
            AnnouncementCategory category2 = AnnouncementCategory.of("업데이트");
            AnnouncementCategory category3 = AnnouncementCategory.of("이벤트");
            AnnouncementCategory category4 = AnnouncementCategory.of("긴급");

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);

            // FavoriteQuestionCategory
            FavoriteQuestionCategory favoriteQuestionCategory1 = FavoriteQuestionCategory.of("장소 관련 문의");
            FavoriteQuestionCategory favoriteQuestionCategory2 = FavoriteQuestionCategory.of("계정 관련 문의");
            FavoriteQuestionCategory favoriteQuestionCategory3 = FavoriteQuestionCategory.of("이벤트 관련 문의");
            FavoriteQuestionCategory favoriteQuestionCategory4 = FavoriteQuestionCategory.of("오류 관련 문의");
            FavoriteQuestionCategory favoriteQuestionCategory5 = FavoriteQuestionCategory.of("기타 문의");
            em.persist(favoriteQuestionCategory1);
            em.persist(favoriteQuestionCategory2);
            em.persist(favoriteQuestionCategory3);
            em.persist(favoriteQuestionCategory4);
            em.persist(favoriteQuestionCategory5);

            // QuestionCategory
            QuestionCategory questionCategory1 = QuestionCategory.of("장소 관련 문의");
            QuestionCategory questionCategory2 = QuestionCategory.of("계정 관련 문의");
            QuestionCategory questionCategory3 = QuestionCategory.of("이벤트 관련 문의");
            QuestionCategory questionCategory4 = QuestionCategory.of("오류 관련 문의");
            QuestionCategory questionCategory5 = QuestionCategory.of("기타 문의");

            em.persist(questionCategory1);
            em.persist(questionCategory2);
            em.persist(questionCategory3);
            em.persist(questionCategory4);
            em.persist(questionCategory5);

            // Category
            Category CE7 = Category.of("CE7", "카페");
            // Category MT1 = Category.of("MT1", "대형 마트");
            // Category CS2 = Category.of("CS2", "편의점");
            // Category PK6 = Category.of("PK6", "주차장");
            // Category OL7 = Category.of("OL7", "주유소, 충전소");
            Category SW8 = Category.of("SW8", "지하철역");
            Category CT1 = Category.of("CT1", "문화시설");
            Category AT4 = Category.of("AT4", "관광명소");
            // Category AD5 = Category.of("AD5", "숙박");
            Category FD6 = Category.of("FD6", "음식점");

            em.persist(CE7);
            // em.persist(MT1);
            // em.persist(CS2);
            // em.persist(PK6);
            // em.persist(OL7);
            em.persist(SW8);
            em.persist(CT1);
            em.persist(AT4);
            // em.persist(AD5);
            em.persist(FD6);

            /*
            // 장소 1000건
            for (int i = 0; i < 1000; i++) {
                Long number = Long.valueOf(i);
                Place place = Place.of(CE7, "장소" + i, Long.toString(number),
                        "url", "서울 관악구 청룡동", "add0", "서울", "관악구", "청룡동", 126.94, 37.47, number * number, number);
                em.persist(place);
            }
            */

            /*
            // 리뷰
            for (int i = 0; i < 1000; i++) {
                Place place = Place.of(CE7, "장소", "1001", "url", "서울 관악구 청룡동", "add0", "서울", "관악구", "청룡동", 10, 10, 1L, 5L);
                em.persist(place);
                Review review = Review.of(user, place, "1001", "title", "description", 5L);
                em.persist(review);
            }
             */

        }
    }
}
