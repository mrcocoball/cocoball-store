package com.dateplanner.admin.consumer.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "description"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_question_category_id")
    private FavoriteQuestionCategory favoriteQuestionCategory;

    private FavoriteAnswer(String title, String description, FavoriteQuestionCategory favoriteQuestionCategory) {
        this.title = title;
        this.description = description;
        this.favoriteQuestionCategory = favoriteQuestionCategory;
    }

    public static FavoriteAnswer of(String title, String description, FavoriteQuestionCategory favoriteQuestionCategory) {
        return new FavoriteAnswer(title, description, favoriteQuestionCategory);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
