package dev.be.moduleapi.support.service;

import dev.be.moduleapi.support.dto.QuestionDto;
import dev.be.modulecore.repositories.support.QuestionRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j(topic = "SERVICE")
@Timed("business.service.qna")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QnaApiService {

    private final QuestionRepository questionRepository;


    public Page<QuestionDto> getQuestions(String nickname, Pageable pageable) {
        return questionRepository.findByUser_Nickname(nickname, pageable).map(question -> QuestionDto.from(question));
    }

    public QuestionDto getQuestion(Long id) {
        return questionRepository.findById(id).map(QuestionDto::from).orElseThrow(EntityNotFoundException::new);
    }

}
