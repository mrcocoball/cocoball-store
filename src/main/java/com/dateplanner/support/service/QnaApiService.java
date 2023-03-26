package com.dateplanner.support.service;

import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.support.dto.QuestionDto;
import com.dateplanner.support.repository.AnswerRepository;
import com.dateplanner.support.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QnaApiService {

    private final QuestionRepository questionRepository;
    private final PaginationService paginationService;


    public Page<QuestionDto> getQuestions(String nickname, Pageable pageable) {
        return paginationService.listToPage(questionRepository.findByUser_Nickname(nickname).stream().map(QuestionDto::from).collect(Collectors.toList()), pageable);
    }

    public QuestionDto getQuestion(Long id) {
        return questionRepository.findById(id).map(QuestionDto::from).orElseThrow(EntityNotFoundException::new);
    }

}
