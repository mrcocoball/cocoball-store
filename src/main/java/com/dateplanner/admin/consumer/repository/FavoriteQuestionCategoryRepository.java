package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.FavoriteQuestionCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteQuestionCategoryRepository extends JpaRepository<FavoriteQuestionCategory, Long> {

    @EntityGraph(attributePaths = "favoriteAnswers")
    @Override
    List<FavoriteQuestionCategory> findAll();

    @EntityGraph(attributePaths = "favoriteAnswers")
    @Override
    Optional<FavoriteQuestionCategory> findById(Long id);

    void deleteById(Long id);

}
