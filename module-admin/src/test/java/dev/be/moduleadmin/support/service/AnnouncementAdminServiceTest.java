package dev.be.moduleadmin.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.advice.exception.AnnouncementNotFoundException;
import dev.be.moduleadmin.support.dto.*;
import dev.be.modulecore.domain.support.Announcement;
import dev.be.modulecore.domain.support.AnnouncementCategory;
import dev.be.modulecore.repositories.support.AnnouncementAdminRepository;
import dev.be.modulecore.repositories.support.AnnouncementCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 공지사항 관리 서비스 - 공지사항 조회 / 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class AnnouncementAdminServiceTest {

    @InjectMocks
    private AnnouncementAdminService sut;

    @Mock
    private AnnouncementAdminRepository announcementAdminRepository;

    @Mock
    private AnnouncementCategoryRepository announcementCategoryRepository;


    @DisplayName("READ - 공지사항 리스트 조회")
    @Test
    public void 공지사항_리스트_조회() {

        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(announcementAdminRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<AnnouncementDto> result = sut.getAnnouncementList(pageable);

        // Then
        assertThat(result).isEmpty();
        then(announcementAdminRepository).should().findAll(pageable);

    }

    @DisplayName("READ - 공지사항 단건 조회")
    @Test
    public void 공지사항_단건_조회_성공() {

        // Given
        Long id = 1L;
        Announcement announcement = Fixture.announcement();
        given(announcementAdminRepository.findById(id)).willReturn(Optional.of(announcement));

        // When
        AnnouncementDto dto = sut.getAnnouncement(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", announcement.getTitle())
                .hasFieldOrPropertyWithValue("description", announcement.getDescription());
        then(announcementAdminRepository).should().findById(id);

    }

    @DisplayName("READ - 공지사항 단건 조회 - 실패(존재하지 않는 공지사항)")
    @Test
    public void 공지사항_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(announcementAdminRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(AnnouncementNotFoundException.class, () -> {
            sut.getAnnouncement(id);
        });
        then(announcementAdminRepository).should().findById(id);

    }

    @DisplayName("READ - 공지사항 카테고리 리스트 조회")
    @Test
    public void 공지사항_카테고리_리스트_조회() {

        // Given
        given(announcementCategoryRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<AnnouncementCategoryDto> result = sut.getAnnouncementCategoryList();

        // Then
        assertThat(result).isEmpty();
        then(announcementCategoryRepository).should().findAll();

    }

    @DisplayName("CREATE - 공지사항 저장")
    @Test
    public void 공지사항_저장_성공() {

        // Given
        Announcement announcement = Fixture.announcement();
        AnnouncementCategory announcementCategory = Fixture.announcementCategory();
        AnnouncementRequestDto dto = Fixture.announcementRequestDto();
        given(announcementCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.of(announcementCategory));
        given(announcementAdminRepository.save(any(Announcement.class))).willReturn(announcement);

        // When
        sut.saveAnnouncement(dto);

        // Then
        then(announcementCategoryRepository).should().findById(dto.getCategoryId());
        then(announcementAdminRepository).should().save(any(Announcement.class));
    }

    @DisplayName("CREATE - 공지사항 저장 - 실패(존재하지 않는 카테고리)")
    @Test
    public void 공지사항_저장_실패() {

        // Given
        AnnouncementRequestDto dto = Fixture.announcementRequestDto();
        given(announcementCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            sut.saveAnnouncement(dto);
        });
        then(announcementCategoryRepository).should().findById(dto.getCategoryId());
    }

    @DisplayName("UPDATE - 공지사항 수정")
    @Test
    public void 공지사항_수정_성공() {

        // Given
        Announcement announcement = Fixture.announcement();
        AnnouncementCategory announcementCategory = Fixture.announcementCategory();
        AnnouncementModifyRequestDto updateRequestDto = Fixture.announcementModifyRequestDto();
        given(announcementAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(announcement));
        given(announcementCategoryRepository.findById(updateRequestDto.getCategoryId())).willReturn(Optional.of(announcementCategory));

        // When
        sut.updateAnnouncement(updateRequestDto);

        // Then
        assertThat(announcement).hasFieldOrPropertyWithValue("title", updateRequestDto.getTitle());
        assertThat(announcement).hasFieldOrPropertyWithValue("description", updateRequestDto.getDescription());
        then(announcementAdminRepository).should().findById(updateRequestDto.getId());
        then(announcementCategoryRepository).should().findById(updateRequestDto.getCategoryId());
    }

    @DisplayName("UPDATE - 공지사항 수정 - 실패(존재하지 않는 공지사항)")
    @Test
    public void 공지사항_수정_실패() {

        // Given
        AnnouncementModifyRequestDto updateRequestDto = Fixture.announcementModifyRequestDto();
        given(announcementAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(AnnouncementNotFoundException.class, () -> {
            sut.updateAnnouncement(updateRequestDto);
        });
        then(announcementAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - 공지사항 수정 - 실패(존재하지 않는 카테고리)")
    @Test
    public void 공지사항_수정_실패_카테고리_오류() {

        // Given
        Announcement announcement = Fixture.announcement();
        AnnouncementModifyRequestDto updateRequestDto = Fixture.announcementModifyRequestDto();
        given(announcementAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(announcement));
        given(announcementCategoryRepository.findById(updateRequestDto.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            sut.updateAnnouncement(updateRequestDto);
        });
        then(announcementAdminRepository).should().findById(updateRequestDto.getId());
        then(announcementCategoryRepository).should().findById(updateRequestDto.getCategoryId());
    }

    @DisplayName("DELETE - 공지사항 삭제")
    @Test
    public void 공지사항_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(announcementAdminRepository).deleteById(id);

        // When
        sut.deleteAnnouncement(id);

        // Then
        then(announcementAdminRepository).should().deleteById(id);

    }

    @DisplayName("READ - 공지사항 카테고리 단건 조회")
    @Test
    public void 공지사항_카테고리_단건_조회_성공() {

        // Given
        Long id = 1L;
        AnnouncementCategory announcementCategory = Fixture.announcementCategory();
        given(announcementCategoryRepository.findById(id)).willReturn(Optional.of(announcementCategory));

        // When
        AnnouncementCategoryDto dto = sut.getAnnouncementCategory(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("categoryName", announcementCategory.getCategoryName());
        then(announcementCategoryRepository).should().findById(id);

    }

    @DisplayName("READ - 공지사항 카테고리 단건 조회 - 실패(존재하지 않는 공지사항 카테고리)")
    @Test
    public void 공지사항_카테고리_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(announcementCategoryRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            sut.getAnnouncementCategory(id);
        });
        then(announcementCategoryRepository).should().findById(id);

    }

    @DisplayName("CREATE - 공지사항 카테고리 저장")
    @Test
    public void 공지사항_카테고리_저장_성공() {

        // Given
        AnnouncementCategory announcementCategory = Fixture.announcementCategory();
        AnnouncementCategoryRequestDto dto = Fixture.announcementCategoryRequestDto();
        given(announcementCategoryRepository.save(any(AnnouncementCategory.class))).willReturn(announcementCategory);

        // When
        sut.saveAnnouncementCategory(dto);

        // Then
        then(announcementCategoryRepository).should().save(any(AnnouncementCategory.class));
    }

    @DisplayName("UPDATE - 공지사항 카테고리 수정")
    @Test
    public void 공지사항_카테고리_수정_성공() {

        // Given
        AnnouncementCategory announcementCategory = Fixture.announcementCategory();
        AnnouncementCategoryModifyRequestDto updateRequestDto = Fixture.announcementCategoryModifyRequestDto();
        given(announcementCategoryRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(announcementCategory));

        // When
        sut.updateAnnouncementCategory(updateRequestDto);

        // Then
        assertThat(announcementCategory).hasFieldOrPropertyWithValue("categoryName", updateRequestDto.getCategoryName());
        then(announcementCategoryRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - 공지사항 카테고리 수정 - 실패(존재하지 않는 공지사항 카테고리)")
    @Test
    public void 공지사항_카테고리_수정_실패() {

        // Given
        AnnouncementCategoryModifyRequestDto updateRequestDto = Fixture.announcementCategoryModifyRequestDto();
        given(announcementCategoryRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            sut.updateAnnouncementCategory(updateRequestDto);
        });
        then(announcementCategoryRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - 공지사항 카테고리 삭제")
    @Test
    public void 공지사항_카테고리_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(announcementCategoryRepository).deleteById(id);

        // When
        sut.deleteAnnouncementCategory(id);

        // Then
        then(announcementCategoryRepository).should().deleteById(id);

    }

}