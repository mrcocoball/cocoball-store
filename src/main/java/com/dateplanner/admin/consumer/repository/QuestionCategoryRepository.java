package com.dateplanner.admin.consumer.repository;

import com.dateplanner.admin.consumer.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
}
