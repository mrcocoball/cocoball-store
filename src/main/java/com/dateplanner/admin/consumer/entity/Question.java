package com.dateplanner.admin.consumer.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "ENTITY")
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "description"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "q_category_id")
    private QuestionCategory questionCategory;

    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @BatchSize(size = 100)
    private List<Answer> answers = new ArrayList<>();

    private Question(String title, String description, User user, QuestionCategory questionCategory) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.questionCategory = questionCategory;
    }

    public static Question of(String title, String description, User user, QuestionCategory questionCategory) {
        return new Question(title, description, user, questionCategory);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

}
