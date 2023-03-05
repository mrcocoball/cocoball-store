package com.dateplanner.admin.consumer.service;

import com.dateplanner.admin.consumer.dto.*;
import com.dateplanner.admin.consumer.entity.Announcement;
import com.dateplanner.admin.consumer.entity.AnnouncementCategory;
import com.dateplanner.admin.consumer.repository.AnnouncementCategoryRepository;
import com.dateplanner.admin.consumer.repository.AnnouncementRepository;
import com.dateplanner.advice.exception.AnnouncementNotFoundException;
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
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCategoryRepository announcementCategoryRepository;


    /**
     * 공지사항 관련
     */

    @Transactional(readOnly = true)
    public List<AnnouncementDto> getAnnouncementList() {

        return announcementRepository.findAll().stream().map(AnnouncementDto::from)
                        .sorted(Comparator.comparing(AnnouncementDto::getCreatedAt).reversed()).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public AnnouncementDto getAnnouncement(Long id) {

        return announcementRepository.findById(id).map(AnnouncementDto::from).orElseThrow(AnnouncementNotFoundException::new);

    }

    public Long saveAnnouncement(AnnouncementRequestDto dto) {

        AnnouncementCategory category = announcementCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        Announcement announcement = announcementRepository.save(dto.toEntity(dto.getTitle(), dto.getDescription(), category));

        return announcement.getId();

    }

    public Long updateAnnouncement(AnnouncementModifyRequestDto dto) {

        Announcement announcement = announcementRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        announcement.changeTitle(dto.getTitle());
        announcement.changeDescription(dto.getDescription());

        if (dto.getCategoryId() != announcement.getAnnouncementCategory().getId()) {
            AnnouncementCategory category = announcementCategoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
            announcement.changeCategory(category);
        }

        return announcement.getId();

    }

    public void deleteAnnouncement(Long id) {

        announcementRepository.deleteById(id);

    }


    /**
     * 공지사항 카테고리 관련
     */

    @Transactional(readOnly = true)
    public List<AnnouncementCategoryDto> getAnnouncementCategoryList() {

        return announcementCategoryRepository.findAll().stream().map(AnnouncementCategoryDto::from).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public AnnouncementCategoryDto getAnnouncementCategory(Long id) {

        return announcementCategoryRepository.findById(id).map(AnnouncementCategoryDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveAnnouncementCategory(AnnouncementCategoryRequestDto dto) {

        AnnouncementCategory category = announcementCategoryRepository.save(dto.toEntity(dto.getCategoryName()));

        return category.getId();

    }

    public Long updateAnnouncementCategory(AnnouncementCategoryModifyRequestDto dto) {

        AnnouncementCategory announcementCategory = announcementCategoryRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        announcementCategory.changeCategoryName(dto.getCategoryName());

        return announcementCategory.getId();

    }

    public void deleteAnnouncementCategory(Long id) {

        announcementCategoryRepository.deleteById(id);

    }

}
