package com.dateplanner.plan.repository;

import com.dateplanner.plan.entity.DetailPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailPlanRepository extends JpaRepository<DetailPlan, Long> {

    void deleteByPlan_Id(Long id);

}
