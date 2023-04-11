package dev.be.moduleadmin.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.support.dto.*;
import dev.be.modulecore.domain.support.FavoriteAnswer;
import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.repositories.support.FavoriteAnswerAdminRepository;
import dev.be.modulecore.repositories.support.FavoriteQuestionCategoryAdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] FAQ 관리 서비스 - FAQ 조회 / 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class FaqAdminServiceTest {

    @InjectMocks
    private FaqAdminService sut;

    @Mock
    private FavoriteQuestionCategoryAdminRepository favoriteQuestionCategoryAdminRepository;

    @Mock
    private FavoriteAnswerAdminRepository favoriteAnswerAdminRepository;


    @DisplayName("READ - FAQ 리스트 조회")
    @Test
    public void FAQ_리스트_조회() {

        // Given
        given(favoriteQuestionCategoryAdminRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<FavoriteQuestionCategoryDto> result = sut.getFavoriteQuestionCategoryList();

        // Then
        assertThat(result).isEmpty();
        then(favoriteQuestionCategoryAdminRepository).should().findAll();

    }

    @DisplayName("READ - FAQ 카테고리 단건 조회")
    @Test
    public void FAQ_카테고리_단건_조회_성공() {

        // Given
        Long id = 1L;
        FavoriteQuestionCategory favoriteQuestionCategory = Fixture.favoriteQuestionCategory();
        given(favoriteQuestionCategoryAdminRepository.findById(id)).willReturn(Optional.of(favoriteQuestionCategory));

        // When
        FavoriteQuestionCategoryDto dto = sut.getFavoriteQuestionCategory(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", favoriteQuestionCategory.getId())
                .hasFieldOrPropertyWithValue("categoryName", favoriteQuestionCategory.getCategoryName());
        then(favoriteQuestionCategoryAdminRepository).should().findById(id);

    }

    @DisplayName("READ - FAQ 카테고리 단건 조회 - 실패(존재하지 않는 카테고리)")
    @Test
    public void FAQ_카테고리_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(favoriteQuestionCategoryAdminRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
           sut.getFavoriteQuestionCategory(id);
        });
        then(favoriteQuestionCategoryAdminRepository).should().findById(id);

    }

    @DisplayName("CREATE - FAQ 카테고리 저장")
    @Test
    public void FAQ_카테고리_저장_성공() {

        // Given
        FavoriteQuestionCategoryRequestDto dto = Fixture.favoriteQuestionCategoryRequestDto();
        given(favoriteQuestionCategoryAdminRepository.save(any(FavoriteQuestionCategory.class))).willReturn(Fixture.favoriteQuestionCategory());

        // When
        sut.saveFavoriteQuestionCategory(dto);

        // Then
        then(favoriteQuestionCategoryAdminRepository).should().save(any(FavoriteQuestionCategory.class));
    }

    @DisplayName("UPDATE - FAQ 카테고리 수정")
    @Test
    public void FAQ_카테고리_수정_성공() {

        // Given
        FavoriteQuestionCategory favoriteQuestionCategory = Fixture.favoriteQuestionCategory();
        FavoriteQuestionCategoryModifyRequestDto updateRequestDto = Fixture.favoriteQuestionCategoryModifyRequestDto();
        given(favoriteQuestionCategoryAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(favoriteQuestionCategory));

        // When
        sut.updateFavoriteQuestionCategory(updateRequestDto);

        // Then
        assertThat(favoriteQuestionCategory).hasFieldOrPropertyWithValue("categoryName", updateRequestDto.getCategoryName());
        then(favoriteQuestionCategoryAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - FAQ 카테고리 수정 - 실패(존재하지 않는 카테고리)")
    @Test
    public void FAQ_카테고리_수정_실패() {

        // Given
        FavoriteQuestionCategoryModifyRequestDto updateRequestDto = Fixture.favoriteQuestionCategoryModifyRequestDto();
        given(favoriteQuestionCategoryAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.updateFavoriteQuestionCategory(updateRequestDto);
        });
        then(favoriteQuestionCategoryAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - FAQ 카테고리 삭제")
    @Test
    public void FAQ_카테고리_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(favoriteQuestionCategoryAdminRepository).deleteById(id);

        // When
        sut.deleteFavoriteQuestionCategory(id);

        // Then
        then(favoriteQuestionCategoryAdminRepository).should().deleteById(id);

    }

    @DisplayName("READ - FAQ 답변 단건 조회")
    @Test
    public void FAQ_답변_단건_조회_성공() {

        // Given
        Long id = 1L;
        FavoriteAnswer favoriteAnswer = Fixture.favoriteAnswer();
        given(favoriteAnswerAdminRepository.findById(id)).willReturn(Optional.of(favoriteAnswer));

        // When
        FavoriteAnswerDto dto = sut.getFavoriteAnswer(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", favoriteAnswer.getTitle())
                .hasFieldOrPropertyWithValue("description", favoriteAnswer.getDescription());
        then(favoriteAnswerAdminRepository).should().findById(id);

    }

    @DisplayName("READ - FAQ 답변 단건 조회 - 실패(존재하지 않는 답변)")
    @Test
    public void FAQ_답변_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(favoriteAnswerAdminRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
           sut.getFavoriteAnswer(id);
        });
        then(favoriteAnswerAdminRepository).should().findById(id);

    }

    @DisplayName("CREATE - FAQ 답변 저장")
    @Test
    public void FAQ_답변_저장_성공() {

        // Given
        FavoriteAnswerRequestDto dto = Fixture.favoriteAnswerRequestDto();
        FavoriteQuestionCategory favoriteQuestionCategory = Fixture.favoriteQuestionCategory();
        given(favoriteQuestionCategoryAdminRepository.findById(dto.getCategoryId())).willReturn(Optional.of(favoriteQuestionCategory));
        given(favoriteAnswerAdminRepository.save(any(FavoriteAnswer.class))).willReturn(Fixture.favoriteAnswer());

        // When
        sut.saveFavoriteAnswer(dto);

        // Then
        then(favoriteQuestionCategoryAdminRepository.findById(dto.getCategoryId()));
        then(favoriteAnswerAdminRepository).should().save(any(FavoriteAnswer.class));
    }

    @DisplayName("CREATE - FAQ 답변 저장 - 실패(존재하지 않는 카테고리)")
    @Test
    public void FAQ_답변_저장_실패() {

        // Given
        FavoriteAnswerRequestDto dto = Fixture.favoriteAnswerRequestDto();
        given(favoriteQuestionCategoryAdminRepository.findById(dto.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.saveFavoriteAnswer(dto);
        });
        then(favoriteQuestionCategoryAdminRepository.findById(dto.getCategoryId()));
    }

    @DisplayName("UPDATE - FAQ 답변 수정")
    @Test
    public void FAQ_답변_수정_성공() {

        // Given
        FavoriteAnswer favoriteAnswer = Fixture.favoriteAnswer();
        FavoriteAnswerModifyRequestDto updateRequestDto = Fixture.favoriteAnswerModifyRequestDto();
        given(favoriteAnswerAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(favoriteAnswer));

        // When
        sut.updateFavoriteAnswer(updateRequestDto);

        // Then
        assertThat(favoriteAnswer)
                .hasFieldOrPropertyWithValue("title", updateRequestDto.getTitle())
                .hasFieldOrPropertyWithValue("description", updateRequestDto.getDescription());
        then(favoriteAnswerAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - FAQ 답변 수정 - 실패(존재하지 않는 답변)")
    @Test
    public void FAQ_답변_수정_실패() {

        // Given
        FavoriteAnswerModifyRequestDto updateRequestDto = Fixture.favoriteAnswerModifyRequestDto();
        given(favoriteAnswerAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.updateFavoriteAnswer(updateRequestDto);
        });
        then(favoriteAnswerAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - FAQ 답변 삭제")
    @Test
    public void FAQ_답변_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(favoriteAnswerAdminRepository).deleteById(id);

        // When
        sut.deleteFavoriteAnswer(id);

        // Then
        then(favoriteAnswerAdminRepository).should().deleteById(id);

    }

}