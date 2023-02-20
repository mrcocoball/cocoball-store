package com.dateplanner.plan.repository;

import com.dateplanner.plan.entity.Plan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @EntityGraph(attributePaths = "user")
    List<Plan> findByUser_Nickname(String nickname);

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Plan> findById(Long id);

}
