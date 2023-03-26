package com.dateplanner.support.service;

import com.dateplanner.admin.consumer.entity.Answer;
import com.dateplanner.admin.consumer.entity.Question;
import com.dateplanner.admin.consumer.entity.QuestionCategory;
import com.dateplanner.admin.consumer.repository.QuestionCategoryRepository;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.support.dto.AnswerRequestDto;
import com.dateplanner.support.dto.QuestionRequestDto;
import com.dateplanner.support.repository.AnswerRepository;
import com.dateplanner.support.repository.QuestionRepository;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class QnaService {

    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;


    public Long saveQuestion(QuestionRequestDto dto) {

        User user = userRepository.findByNickname(dto.getNickname()).orElseThrow(UserNotFoundApiException::new);
        QuestionCategory questionCategory = questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        Question question = questionRepository.save(dto.toEntity(dto.getTitle(), dto.getDescription(), user, questionCategory));

        return question.getId();

    }

    public Long updateQuestion(QuestionRequestDto dto) {

        Question question = questionRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        question.changeTitle(dto.getTitle());
        question.changeDescription(dto.getDescription());
        if (!question.getQuestionCategory().getId().equals(dto.getCategoryId())) {
            question.changeCategory(questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new));
        }

        return question.getId();

    }

    public void deleteQuestion(Long id) {

        questionRepository.deleteById(id);

    }

    public Long saveAnswer(AnswerRequestDto dto) {

        User user = userRepository.findByNickname(dto.getNickname()).orElseThrow(UserNotFoundApiException::new);
        Question question = questionRepository.findById(dto.getQid()).orElseThrow(EntityNotFoundException::new);

        Answer answer = answerRepository.save(dto.toEntity(user, question, dto.getDescription()));

        return answer.getId();

    }

}
