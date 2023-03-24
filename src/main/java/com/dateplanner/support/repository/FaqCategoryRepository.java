package com.dateplanner.support.repository;

import com.dateplanner.admin.consumer.entity.FavoriteQuestionCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaqCategoryRepository extends JpaRepository<FavoriteQuestionCategory, Long> {

    @EntityGraph(attributePaths = "favoriteAnswers")
    @Override
    List<FavoriteQuestionCategory> findAll();

    @EntityGraph(attributePaths = "favoriteAnswers")
    @Override
    Optional<FavoriteQuestionCategory> findById(Long id);

}
