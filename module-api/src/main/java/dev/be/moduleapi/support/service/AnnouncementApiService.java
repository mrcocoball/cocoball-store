package dev.be.moduleapi.support.service;

import dev.be.moduleapi.advice.exception.AnnouncementNotFoundException;
import dev.be.moduleapi.support.dto.AnnouncementDto;
import dev.be.moduleapi.support.repository.AnnouncementCustomRepository;
import dev.be.modulecore.repositories.support.AnnouncementRepository;
import dev.be.modulecore.service.PaginationService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@Timed("business.service.announcement")
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
