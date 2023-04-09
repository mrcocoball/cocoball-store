package dev.be.moduleapi.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.support.dto.FaqCategoryDto;
import dev.be.moduleapi.support.dto.FaqDto;
import dev.be.modulecore.domain.support.FavoriteAnswer;
import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
import dev.be.modulecore.repositories.support.FaqCategoryRepository;
import dev.be.modulecore.repositories.support.FaqRepository;
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

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] FAQ 화면 처리 서비스 - FAQ 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FaqApiServiceTest {

    @Autowired
    private FaqApiService sut;

    @MockBean
    private FaqRepository faqRepository;

    @MockBean
    private FaqCategoryRepository faqCategoryRepository;

    @DisplayName("READ - FAQ 리스트 조회")
    @Test
    public void FAQ_리스트_조회() {

        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(faqCategoryRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<FaqCategoryDto> result = sut.getFaqCategoryList(pageable);

        // Then
        assertThat(result).isEmpty();
        then(faqCategoryRepository).should().findAll(pageable);

    }

    @DisplayName("READ - FAQ 카테고리 단건 조회")
    @Test
    public void FAQ_카테고리_단건_조회_성공() {

        // Given
        Long id = 1L;
        FavoriteQuestionCategory favoriteQuestionCategory = Fixture.favoriteQuestionCategory();
        given(faqCategoryRepository.findById(id)).willReturn(Optional.of(favoriteQuestionCategory));

        // When
        FaqCategoryDto dto = sut.getFaqCategory(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", favoriteQuestionCategory.getId())
                .hasFieldOrPropertyWithValue("categoryName", favoriteQuestionCategory.getCategoryName());
        then(faqCategoryRepository).should().findById(id);

    }

    @DisplayName("READ - FAQ 카테고리 단건 조회 - 실패(존재하지 않는 카테고리)")
    @Test
    public void FAQ_카테고리_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(faqCategoryRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
           sut.getFaqCategory(id);
        });
        then(faqCategoryRepository).should().findById(id);

    }

    @DisplayName("READ - FAQ 답변 단건 조회")
    @Test
    public void FAQ_답변_단건_조회_성공() {

        // Given
        Long id = 1L;
        FavoriteAnswer favoriteAnswer = Fixture.favoriteAnswer();
        given(faqRepository.findById(id)).willReturn(Optional.of(favoriteAnswer));

        // When
        FaqDto dto = sut.getFaq(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", favoriteAnswer.getTitle())
                .hasFieldOrPropertyWithValue("description", favoriteAnswer.getDescription());
        then(faqRepository).should().findById(id);

    }

    @DisplayName("READ - FAQ 답변 단건 조회 - 실패(존재하지 않는 답변)")
    @Test
    public void FAQ_답변_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(faqRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
           sut.getFaq(id);
        });
        then(faqRepository).should().findById(id);

    }

}