package com.dateplanner.review.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.image.entity.Image;
import com.dateplanner.place.entity.Place;
import com.dateplanner.admin.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "kpid"),
        @Index(columnList = "createdAt")
})
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

    @OneToMany(mappedBy = "review",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Image> images = new HashSet<>();


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

    public void addImage(String uuid, String fileName) {

        Image image = Image.builder()
                .uuid(uuid)
                .fileName(fileName)
                .review(this)
                .ord(images.size())
                .build();
        images.add(image);
    }

    public void clearImages() {

        images.forEach(image -> image.changeReview(null));
        this.images.clear();
    }

}
