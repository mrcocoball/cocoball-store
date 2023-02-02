package com.dateplanner.image.entity;

import com.dateplanner.review.entity.Review;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Comparable<Image> {

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Review review;


    private Image(Review review, String uuid, String fileName, int ord) {
        this.review = review;
        this.uuid = uuid;
        this.fileName = fileName;
        this.ord = ord;
    }

    public static Image of(Review review, String uuid, String fileName, int ord) {
        return new Image(review, uuid, fileName, ord);
    }

    @Override
    public int compareTo(Image other) {
        return this.ord - other.ord;
    }

    public void changeReview(Review review) {
        this.review = null;
    }

}
