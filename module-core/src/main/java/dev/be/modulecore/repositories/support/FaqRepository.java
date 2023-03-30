package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.FavoriteAnswer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaqRepository extends JpaRepository<FavoriteAnswer, Long> {

    @EntityGraph(attributePaths = "favoriteQuestionCategory")
    @Override
    Optional<FavoriteAnswer> findById(Long id);

}
