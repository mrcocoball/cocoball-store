package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "questionCategory"})
    Optional<Question> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"user", "questionCategory"})
    List<Question> findAll();

    @EntityGraph(attributePaths = {"user", "questionCategory"})
    List<Question> findByUser_Nickname(String nickname);

    void deleteById(Long id);

}
