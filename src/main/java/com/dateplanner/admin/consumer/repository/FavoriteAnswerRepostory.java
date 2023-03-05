package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.FavoriteAnswer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteAnswerRepostory extends JpaRepository<FavoriteAnswer, Long> {

    @EntityGraph(attributePaths = "favoriteQuestionCategory")
    @Override
    Optional<FavoriteAnswer> findById(Long id);

    void deleteById(Long id);

}
