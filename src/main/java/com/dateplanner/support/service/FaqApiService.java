package com.dateplanner.support.service;

import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.support.dto.FaqCategoryDto;
import com.dateplanner.support.dto.FaqDto;
import com.dateplanner.support.repository.FaqCategoryRepository;
import com.dateplanner.support.repository.FaqRepository;
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
public class FaqApiService {

    private final FaqRepository faqRepository;
    private final FaqCategoryRepository faqCategoryRepository;
    private final PaginationService paginationService;


    public Page<FaqCategoryDto> getFaqCategoryList(Pageable pageable) {

        return paginationService.listToPage(faqCategoryRepository.findAll().stream().map(FaqCategoryDto::from).collect(Collectors.toList()), pageable);

    }

    public FaqCategoryDto getFaqCategory(Long id) {

        return faqCategoryRepository.findById(id).map(FaqCategoryDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public FaqDto getFaq(Long id) {

        return faqRepository.findById(id).map(FaqDto::from).orElseThrow(EntityNotFoundException::new);

    }

}
