package com.dateplanner.plan.entity;

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
public class DetailPlan extends BaseTimeEntity implements Comparable<DetailPlan> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place")
    private Place place;

    private String kpid;

    private int ord;

    private DetailPlan(Plan plan, Place place, String kpid, int ord) {
        this.plan = plan;
        this.place = place;
        this.kpid = kpid;
        this.ord = ord;
    }

    public static DetailPlan of(Plan plan, Place place, String kpid, int ord) {
        return new DetailPlan(plan, place, kpid, ord);
    }

    @Override
    public int compareTo(DetailPlan other) {
        return this.ord - other.ord;
    }

	public void changePlan(Plan plan) {
		this.plan = null;
	}

}
