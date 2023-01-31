package com.dateplanner.plan.repository;

import com.dateplanner.plan.entity.DetailPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailPlanRepository extends JpaRepository<DetailPlan, Long> {

    @Override
    @EntityGraph(attributePaths = {"plan", "place"})
    Optional<DetailPlan> findById(Long id);

    void deleteByPlan_Id(Long id);

}
