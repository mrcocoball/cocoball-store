package com.dateplanner.bookmark.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.place.entity.Place;
import com.dateplanner.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "kpid"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Bookmark extends BaseTimeEntity {

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

    private Bookmark(User user, Place place, String kpid) {
        this.user = user;
        this.place = place;
        this.kpid = kpid;
    }

    public static Bookmark of(User user, Place place, String kpid) {
        return new Bookmark(user, place, kpid);
    }

}
