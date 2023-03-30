package dev.be.modulecore.domain.support;

import dev.be.modulecore.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "categoryName"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteQuestionCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_question_category_id")
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "favoriteQuestionCategory",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @BatchSize(size = 100)
    private List<FavoriteAnswer> favoriteAnswers = new ArrayList<>();

    private FavoriteQuestionCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static FavoriteQuestionCategory of(String categoryName) {
        return new FavoriteQuestionCategory(categoryName);
    }

    public void changeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void addFavoriteAnswer(FavoriteAnswer favoriteAnswer) {
        favoriteAnswers.add(favoriteAnswer);
        favoriteAnswer.setFavoriteQuestionCategory(this);
    }

    public void removeFavoriteAnswer(FavoriteAnswer favoriteAnswer) {
        favoriteAnswers.remove(favoriteAnswer);
        favoriteAnswer.setFavoriteQuestionCategory(null);
    }

}
