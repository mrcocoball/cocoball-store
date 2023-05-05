package dev.be.moduleapi;

import dev.be.modulecore.domain.place.Category;
import dev.be.modulecore.domain.support.AnnouncementCategory;
import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.repositories.place.CategoryRepository;
import dev.be.modulecore.repositories.support.AnnouncementCategoryRepository;
import dev.be.modulecore.repositories.support.FavoriteQuestionCategoryAdminRepository;
import dev.be.modulecore.repositories.support.QuestionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

        @Autowired
        AnnouncementCategoryRepository announcementCategoryRepository;

        @Autowired
        FavoriteQuestionCategoryAdminRepository favoriteQuestionCategoryAdminRepository;

        @Autowired
        QuestionCategoryRepository questionCategoryRepository;

        @Autowired
        CategoryRepository categoryRepository;

        @Transactional
        public void init() {

            if (announcementCategoryRepository.findAll().isEmpty()) {

                AnnouncementCategory category1 = AnnouncementCategory.of("일반");
                AnnouncementCategory category2 = AnnouncementCategory.of("업데이트");
                AnnouncementCategory category3 = AnnouncementCategory.of("이벤트");
                AnnouncementCategory category4 = AnnouncementCategory.of("긴급");

                em.persist(category1);
                em.persist(category2);
                em.persist(category3);
                em.persist(category4);

            }

            if (favoriteQuestionCategoryAdminRepository.findAll().isEmpty()) {

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

            }

            if (questionCategoryRepository.findAll().isEmpty()) {

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

            }

            if (categoryRepository.findAll().isEmpty()) {

                Category CE7 = Category.of("CE7", "카페");
                Category SW8 = Category.of("SW8", "지하철역");
                Category CT1 = Category.of("CT1", "문화시설");
                Category AT4 = Category.of("AT4", "관광명소");
                Category FD6 = Category.of("FD6", "음식점");

                em.persist(CE7);
                em.persist(SW8);
                em.persist(CT1);
                em.persist(AT4);
                em.persist(FD6);

            }

        }
    }
}
