package com.dateplanner.admin.consumer.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnouncementCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_category_id")
    private Long id;

    private String categoryName;

    private AnnouncementCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static AnnouncementCategory of(String category) {
        return new AnnouncementCategory(category);
    }

    public void changeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
