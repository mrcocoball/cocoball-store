package com.dateplanner.admin.consumer.service;

import com.dateplanner.admin.consumer.dto.*;
import com.dateplanner.admin.consumer.entity.Answer;
import com.dateplanner.admin.consumer.entity.Question;
import com.dateplanner.admin.consumer.entity.QuestionCategory;
import com.dateplanner.admin.consumer.repository.AnswerRepository;
import com.dateplanner.admin.consumer.repository.QuestionCategoryRepository;
import com.dateplanner.admin.consumer.repository.QuestionRepository;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    /**
     * 질문 관련
     */

    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionList() {

        return questionRepository.findAll().stream().map(QuestionDto::from)
                .sorted(Comparator.comparing(QuestionDto::getCreatedAt).reversed()).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestion(Long id) {

        return questionRepository.findById(id).map(QuestionDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveQuestion(QuestionRequestDto dto) {

        User user = userRepository.findByNickname(dto.getUsername()).orElseThrow(UserNotFoundApiException::new);
        QuestionCategory questionCategory = questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);

        Question question = questionRepository.save(dto.toEntity(dto.getTitle(), dto.getDescription(), user, questionCategory));

        return question.getId();

    }

    public Long updateQuestion(QuestionModifyRequestDto dto) {

        Question question = questionRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        question.changeTitle(dto.getTitle());
        question.changeDescription(dto.getDescription());

        if (dto.getCategoryId() != question.getQuestionCategory().getId()) {
            QuestionCategory category = questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
            question.changeCategory(category);
        }

        return question.getId();

    }

    public void deleteQuestion(Long id) {

        questionRepository.deleteById(id);

    }


    /**
     * 답변 관련
     */

    @Transactional(readOnly = true)
    public AnswerDto getAnswer(Long id) {

        return answerRepository.findById(id).map(AnswerDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveAnswer(AnswerRequestDto dto) {

        User user = userRepository.findByNickname(dto.getUsername()).orElseThrow(UserNotFoundApiException::new);
        Question question = questionRepository.findById(dto.getQid()).orElseThrow(EntityNotFoundException::new);

        Answer answer = answerRepository.save(dto.toEntity(user, question, dto.getDescription()));
        question.addAnswer(answer);

        return answer.getId();

    }

    public void deleteAnswer(Long id) {

        answerRepository.deleteById(id);

    }


    /**
     * 카테고리 관련
     */


    @Transactional(readOnly = true)
    public QuestionCategoryDto getQuestionCategory(Long id) {

        return questionCategoryRepository.findById(id).map(QuestionCategoryDto::from).orElseThrow(EntityNotFoundException::new);

    }

    @Transactional(readOnly = true)
    public List<QuestionCategoryDto> getQuestionCategoryList() {

        return questionCategoryRepository.findAll().stream().map(QuestionCategoryDto::from).collect(Collectors.toList());

    }

    public Long saveQuestionCategory(QuestionCategoryRequestDto dto) {

        QuestionCategory questionCategory = questionCategoryRepository.save(dto.toEntity(dto.getCategoryName()));

        return questionCategory.getId();

    }

    public Long updateQuestionCategory(QuestionCategoryModifyRequestDto dto) {

        QuestionCategory questionCategory = questionCategoryRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        questionCategory.changeCategoryName(dto.getCategoryName());

        return questionCategory.getId();

    }

    public void deleteQuestionCategory(Long id) {

        questionCategoryRepository.deleteById(id);

    }

}
