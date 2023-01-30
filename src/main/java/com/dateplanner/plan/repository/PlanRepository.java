package com.dateplanner.plan.repository;

import com.dateplanner.plan.entity.Plan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @EntityGraph(attributePaths = "user")
    List<Plan> findByUser_Uid(String uid);

}
