package dev.be.modulecore.domain.plan;

import dev.be.modulecore.domain.BaseTimeEntity;
import dev.be.modulecore.domain.place.Place;
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

    public void changePlace(Place place) { this.place = place; }

    public void changeKpid(String kpid) { this.kpid = kpid; }

    public void changeOrd(int ord) {
        this.ord = ord;
    }

}
