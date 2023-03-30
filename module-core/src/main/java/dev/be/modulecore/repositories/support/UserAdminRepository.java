package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAdminRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roleSet")
    @Override
    List<User> findAll();

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByUid(Long uid);


}

