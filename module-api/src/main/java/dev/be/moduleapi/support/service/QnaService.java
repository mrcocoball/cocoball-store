package dev.be.moduleapi.support.service;

import dev.be.moduleapi.advice.exception.UserNotFoundApiException;
import dev.be.moduleapi.support.dto.AnswerRequestDto;
import dev.be.moduleapi.support.dto.QuestionRequestDto;
import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.AnswerRepository;
import dev.be.modulecore.repositories.support.QuestionCategoryRepository;
import dev.be.modulecore.repositories.support.QuestionRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
        if (question.getQuestionCategory().getId() != null && !question.getQuestionCategory().getId().equals(dto.getCategoryId())) {
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
