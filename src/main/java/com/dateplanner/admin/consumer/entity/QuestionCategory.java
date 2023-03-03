package com.dateplanner.admin.consumer.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "categoryName"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_category_id")
    private Long id;

    private String categoryName;

    private QuestionCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static QuestionCategory of(String categoryName) {
        return new QuestionCategory(categoryName);
    }
}
