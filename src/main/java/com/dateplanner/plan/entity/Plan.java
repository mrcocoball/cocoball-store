package com.dateplanner.plan.entity;

import com.dateplanner.constant.entity.BaseTimeEntity;
import com.dateplanner.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "ENTITY")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "plan",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<DetailPlan> detailPlans = new ArrayList<>();


    private Plan(User user, String title) {
        this.user = user;
        this.title = title;
    }

    public static Plan of(User user, String title) {
        return new Plan(user, title);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void addDetailPlans(DetailPlan detailPlan) {
        this.detailPlans.add(detailPlan);
    }

    public void removeDetailPlans(DetailPlan detailPlan) {
        this.detailPlans.remove(detailPlan);
    }

    public void clearDetailPlans() {
        detailPlans.forEach(detailPlan -> detailPlan.changePlan(null));
        this.detailPlans.clear();
    }

}
