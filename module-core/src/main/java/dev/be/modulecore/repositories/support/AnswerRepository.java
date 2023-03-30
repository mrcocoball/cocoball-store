package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "question"})
    Optional<Answer> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"user", "question"})
    List<Answer> findAll();

    void deleteById(Long id);

}
