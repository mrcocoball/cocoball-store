package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.UserPasswordRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPasswordRequestRepository extends JpaRepository<UserPasswordRequest, Long> {

    @EntityGraph(attributePaths = "user")
    @Override
    List<UserPasswordRequest> findAll();

    @EntityGraph(attributePaths = "user")
    @Override
    Optional<UserPasswordRequest> findById(Long id);

    void deleteById(Long id);
}
