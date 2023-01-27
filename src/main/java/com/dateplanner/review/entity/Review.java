package com.dateplanner.review.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.place.entity.Place;
import com.dateplanner.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "kpid")
    private String kpid;

    private String title;

    private String description;

    private Long reviewScore;

    // TODO : 추후 이미지 추가

    private Review(User user, Place place, String kpid, String title, String description, Long reviewScore) {
        this.user = user;
        this.place = place;
        this.kpid = kpid;
        this.title = title;
        this.description = description;
        this.reviewScore = reviewScore;
    }

    public static Review of(User user, Place place, String kpid, String title, String description, Long reviewScore) {
        return new Review(user, place, kpid, title, description, reviewScore);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeReviewScore(Long score) {
        this.reviewScore = score;
    }

}
