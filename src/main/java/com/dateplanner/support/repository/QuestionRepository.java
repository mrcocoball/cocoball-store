package com.dateplanner.support.repository;

import com.dateplanner.admin.consumer.entity.Question;
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
