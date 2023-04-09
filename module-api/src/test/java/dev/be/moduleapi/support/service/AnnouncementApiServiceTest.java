package dev.be.moduleapi.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.advice.exception.AnnouncementNotFoundException;
import dev.be.moduleapi.support.dto.AnnouncementDto;
import dev.be.moduleapi.support.repository.AnnouncementCustomRepository;
import dev.be.modulecore.domain.support.Announcement;
import dev.be.modulecore.repositories.support.AnnouncementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 공지사항 화면 처리 서비스 - 공지사항 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AnnouncementApiServiceTest {

    @Autowired
    private AnnouncementApiService sut;

    @MockBean
    private AnnouncementRepository announcementRepository;

    @MockBean
    private AnnouncementCustomRepository announcementCustomRepository;

    @DisplayName("READ - 공지사항 리스트 조회")
    @Test
    public void 공지사항_리스트_조회() {

        String condition = "title";
        Long categoryId = 1L;
        String keyword = "test";

        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(announcementCustomRepository.announcementList(condition, categoryId, keyword)).willReturn(Collections.emptyList());

        // When
        Page<AnnouncementDto> result = sut.getAnnouncementListWithCondition(condition, categoryId, keyword, pageable);

        // Then
        assertThat(result).isEmpty();
        then(announcementCustomRepository).should().announcementList(condition, categoryId, keyword);

    }

    @DisplayName("READ - 공지사항 단건 조회")
    @Test
    public void 공지사항_단건_조회_성공() {

        // Given
        Long id = 1L;
        Announcement announcement = Fixture.announcement();
        given(announcementRepository.findById(id)).willReturn(Optional.of(announcement));

        // When
        AnnouncementDto dto = sut.getAnnouncement(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", announcement.getTitle())
                .hasFieldOrPropertyWithValue("description", announcement.getDescription());
        then(announcementRepository).should().findById(id);

    }

    @DisplayName("READ - 공지사항 단건 조회 - 실패(존재하지 않는 공지사항)")
    @Test
    public void 공지사항_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(announcementRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(AnnouncementNotFoundException.class, () -> {
            sut.getAnnouncement(id);
        });
        then(announcementRepository).should().findById(id);

    }


}