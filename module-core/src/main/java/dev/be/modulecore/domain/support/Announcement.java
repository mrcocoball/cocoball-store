package dev.be.modulecore.domain.support;

import dev.be.modulecore.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class Announcement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_category_id")
    private AnnouncementCategory announcementCategory;

    private Announcement(String title, String description, AnnouncementCategory announcementCategory) {
        this.title = title;
        this.description = description;
        this.announcementCategory = announcementCategory;
    }

    public static Announcement of(String title, String description, AnnouncementCategory announcementCategory) {
        return new Announcement(title, description, announcementCategory);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeCategory(AnnouncementCategory category) {
        this.announcementCategory = category;
    }

}
