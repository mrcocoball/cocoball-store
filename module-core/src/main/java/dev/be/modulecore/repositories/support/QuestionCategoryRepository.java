package dev.be.modulecore.repositories.support;

import dev.be.modulecore.domain.support.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

    void deleteById(Long id);

}
