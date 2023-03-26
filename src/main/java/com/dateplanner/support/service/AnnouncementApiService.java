package com.dateplanner.support.service;

import com.dateplanner.advice.exception.AnnouncementNotFoundException;
import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.support.dto.AnnouncementDto;
import com.dateplanner.support.repository.AnnouncementCustomRepository;
import com.dateplanner.support.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AnnouncementApiService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCustomRepository announcementCustomRepository;
    private final PaginationService paginationService;


    public Page<AnnouncementDto> getAnnouncementListWithCondition(String condition, Long categoryId, String keyword, Pageable pageable) {

        return paginationService.listToPage(announcementCustomRepository.announcementList(condition, categoryId, keyword), pageable);

    }

    public AnnouncementDto getAnnouncement(Long id) {

        return announcementRepository.findById(id).map(AnnouncementDto::from).orElseThrow(AnnouncementNotFoundException::new);

    }

}
