package dev.be.moduleapi.support.service;

import dev.be.moduleapi.support.dto.FaqCategoryDto;
import dev.be.moduleapi.support.dto.FaqDto;
import dev.be.modulecore.repositories.support.FaqCategoryRepository;
import dev.be.modulecore.repositories.support.FaqRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j(topic = "SERVICE")
@Timed("business.service.faq")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FaqApiService {

    private final FaqRepository faqRepository;
    private final FaqCategoryRepository faqCategoryRepository;


    public Page<FaqCategoryDto> getFaqCategoryList(Pageable pageable) {

        return faqCategoryRepository.findAll(pageable).map(favoriteQuestionCategory -> FaqCategoryDto.from(favoriteQuestionCategory));

    }

    public FaqCategoryDto getFaqCategory(Long id) {

        return faqCategoryRepository.findById(id).map(FaqCategoryDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public FaqDto getFaq(Long id) {

        return faqRepository.findById(id).map(FaqDto::from).orElseThrow(EntityNotFoundException::new);

    }

}
