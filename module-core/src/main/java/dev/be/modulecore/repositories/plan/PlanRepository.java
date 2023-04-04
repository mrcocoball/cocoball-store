package dev.be.modulecore.repositories.plan;

import dev.be.modulecore.domain.plan.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    /* 테스트 수정 후 삭제 필요 */
    @EntityGraph(attributePaths = "user")
    List<Plan> findByUser_Nickname(String nickname);

    @EntityGraph(attributePaths = "user")
    Page<Plan> findByUser_Nickname(String nickname, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Plan> findById(Long id);

}
