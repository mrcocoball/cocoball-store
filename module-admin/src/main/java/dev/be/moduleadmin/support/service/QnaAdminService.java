package dev.be.moduleadmin.support.service;

import dev.be.moduleadmin.advice.exception.UserNotFoundApiException;
import dev.be.moduleadmin.support.dto.*;
import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.AnswerAdminRepository;
import dev.be.modulecore.repositories.support.QuestionAdminRepository;
import dev.be.modulecore.repositories.support.QuestionCategoryRepository;
import dev.be.modulecore.repositories.user.UserRepository;
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
public class QnaAdminService {

    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionAdminRepository questionAdminRepository;
    private final AnswerAdminRepository answerAdminRepository;
    private final UserRepository userRepository;

    /**
     * 질문 관련
     */

    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionList() {

        return questionAdminRepository.findAll().stream().map(QuestionDto::from)
                .sorted(Comparator.comparing(QuestionDto::getCreatedAt).reversed()).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestion(Long id) {

        return questionAdminRepository.findById(id).map(QuestionDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveQuestion(QuestionRequestDto dto) {

        User user = userRepository.findByNickname(dto.getUsername()).orElseThrow(UserNotFoundApiException::new);
        QuestionCategory questionCategory = questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);

        Question question = questionAdminRepository.save(dto.toEntity(dto.getTitle(), dto.getDescription(), user, questionCategory));

        return question.getId();

    }

    public Long updateQuestion(QuestionModifyRequestDto dto) {

        Question question = questionAdminRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        question.changeTitle(dto.getTitle());
        question.changeDescription(dto.getDescription());

        if (dto.getCategoryId() != question.getQuestionCategory().getId()) {
            QuestionCategory category = questionCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
            question.changeCategory(category);
        }

        return question.getId();

    }

    public void deleteQuestion(Long id) {

        questionAdminRepository.deleteById(id);

    }


    /**
     * 답변 관련
     */

    @Transactional(readOnly = true)
    public AnswerDto getAnswer(Long id) {

        return answerAdminRepository.findById(id).map(AnswerDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveAnswer(AnswerRequestDto dto) {

        User user = userRepository.findByNickname(dto.getUsername()).orElseThrow(UserNotFoundApiException::new);
        Question question = questionAdminRepository.findById(dto.getQid()).orElseThrow(EntityNotFoundException::new);

        Answer answer = answerAdminRepository.save(dto.toEntity(user, question, dto.getDescription()));
        question.addAnswer(answer);

        return answer.getId();

    }

    public void deleteAnswer(Long id) {

        answerAdminRepository.deleteById(id);

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
