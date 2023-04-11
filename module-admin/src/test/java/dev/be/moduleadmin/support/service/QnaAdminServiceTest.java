package dev.be.moduleadmin.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.support.dto.*;
import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.AnswerAdminRepository;
import dev.be.modulecore.repositories.support.QuestionAdminRepository;
import dev.be.modulecore.repositories.support.QuestionCategoryRepository;
import dev.be.modulecore.repositories.user.UserRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] QNA 관리 서비스 - QNA 조회 / 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class QnaAdminServiceTest {

    @InjectMocks
    private QnaAdminService sut;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionCategoryRepository questionCategoryRepository;

    @Mock
    private QuestionAdminRepository questionAdminRepository;

    @Mock
    private AnswerAdminRepository answerAdminRepository;


    @DisplayName("READ - QNA 리스트 조회")
    @Test
    public void QNA_리스트_조회() {

        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(questionAdminRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<QuestionDto> result = sut.getQuestionList(pageable);

        // Then
        assertThat(result).isEmpty();
        then(questionAdminRepository).should().findAll(pageable);

    }

    @DisplayName("READ - QNA 단건 조회")
    @Test
    public void QNA_단건_조회_성공() {

        // Given
        Long id = 1L;
        Question question = Fixture.question();
        given(questionAdminRepository.findById(id)).willReturn(Optional.of(question));

        // When
        QuestionDto dto = sut.getQuestion(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", question.getTitle())
                .hasFieldOrPropertyWithValue("description", question.getDescription());
        then(questionAdminRepository).should().findById(id);

    }

    @DisplayName("READ - QNA 단건 조회 - 실패(존재하지 않는 질문)")
    @Test
    public void QNA_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(questionAdminRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.getQuestion(id);
        });
        then(questionAdminRepository).should().findById(id);

    }

    @DisplayName("CREATE - 질문 저장")
    @Test
    public void 질문_저장_성공() {

        // Given
        User user = Fixture.user();
        QuestionCategory questionCategory = Fixture.questionCategory();
        QuestionRequestDto dto = Fixture.questionRequestDto();
        given(userRepository.findByNickname(dto.getUsername())).willReturn(Optional.of(user));
        given(questionCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.of(questionCategory));
        given(questionAdminRepository.save(any(Question.class))).willReturn(Fixture.question());

        // When
        sut.saveQuestion(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getUsername());
        then(questionCategoryRepository).should().findById(dto.getCategoryId());
        then(questionAdminRepository).should().save(any(Question.class));
    }

    @DisplayName("CREATE - 질문 저장 - 실패(존재하지 않는 카테고리)")
    @Test
    public void 질문_저장_실패() {

        // Given
        User user = Fixture.user();
        QuestionRequestDto dto = Fixture.questionRequestDto();
        given(userRepository.findByNickname(dto.getUsername())).willReturn(Optional.of(user));
        given(questionCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.saveQuestion(dto);
        });
        then(userRepository).should().findByNickname(dto.getUsername());
        then(questionCategoryRepository).should().findById(dto.getCategoryId());
    }

    @DisplayName("UPDATE - 질문 수정")
    @Test
    public void 질문_수정_성공() {

        // Given
        Question question = Fixture.question();
        QuestionModifyRequestDto updateRequestDto = Fixture.questionModifyRequestDto();
        given(questionAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(question));

        // When
        sut.updateQuestion(updateRequestDto);

        // Then
        assertThat(question).hasFieldOrPropertyWithValue("title", updateRequestDto.getTitle());
        assertThat(question).hasFieldOrPropertyWithValue("description", updateRequestDto.getDescription());
        then(questionAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - 질문 수정 - 실패(존재하지 않는 질문)")
    @Test
    public void 질문_수정_실패() {

        // Given
        QuestionModifyRequestDto updateRequestDto = Fixture.questionModifyRequestDto();
        given(questionAdminRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.updateQuestion(updateRequestDto);
        });
        then(questionAdminRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - 질문 삭제")
    @Test
    public void 질문_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(questionAdminRepository).deleteById(id);

        // When
        sut.deleteQuestion(id);

        // Then
        then(questionAdminRepository).should().deleteById(id);

    }

    @DisplayName("CREATE - 답변 저장")
    @Test
    public void 답변_저장_성공() {

        // Given
        User user = Fixture.user();
        Question question = Fixture.question();
        AnswerRequestDto dto = Fixture.answerRequestDto();
        given(userRepository.findByNickname(dto.getUsername())).willReturn(Optional.of(user));
        given(questionAdminRepository.findById(dto.getQid())).willReturn(Optional.of(question));
        given(answerAdminRepository.save(any(Answer.class))).willReturn(Fixture.answer());

        // When
        sut.saveAnswer(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getUsername());
        then(questionAdminRepository).should().findById(dto.getQid());
        then(answerAdminRepository).should().save(any(Answer.class));
    }

    @DisplayName("CREATE - 답변 저장 - 실패(존재하지 않는 질문)")
    @Test
    public void 답변_저장_실패() {

        // Given
        User user = Fixture.user();
        AnswerRequestDto dto = Fixture.answerRequestDto();
        given(userRepository.findByNickname(dto.getUsername())).willReturn(Optional.of(user));
        given(questionAdminRepository.findById(dto.getQid())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.saveAnswer(dto);
        });
        then(userRepository).should().findByNickname(dto.getUsername());
        then(questionAdminRepository).should().findById(dto.getQid());
    }

    @DisplayName("READ - QNA 카테고리 리스트 조회")
    @Test
    public void QNA_카테고리_리스트_조회() {

        // Given
        given(questionCategoryRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<QuestionCategoryDto> result = sut.getQuestionCategoryList();

        // Then
        assertThat(result).isEmpty();
        then(questionCategoryRepository).should().findAll();

    }

    @DisplayName("READ - QNA 카테고리 단건 조회")
    @Test
    public void QNA_카테고리_단건_조회_성공() {

        // Given
        Long id = 1L;
        QuestionCategory questionCategory = Fixture.questionCategory();
        given(questionCategoryRepository.findById(id)).willReturn(Optional.of(questionCategory));

        // When
        QuestionCategoryDto dto = sut.getQuestionCategory(id);

        // Then
        assertThat(dto).hasFieldOrPropertyWithValue("categoryName", questionCategory.getCategoryName());
        then(questionCategoryRepository).should().findById(id);

    }

    @DisplayName("READ - QNA 카테고리 단건 조회 - 실패(존재하지 않는 카테고리)")
    @Test
    public void QNA_카테고리_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(questionCategoryRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.getQuestionCategory(id);
        });
        then(questionCategoryRepository).should().findById(id);

    }

    @DisplayName("CREATE - 카테고리 저장")
    @Test
    public void 카테고리_저장_성공() {

        // Given
        QuestionCategoryRequestDto dto = Fixture.questionCategoryRequestDto();
        given(questionCategoryRepository.save(any(QuestionCategory.class))).willReturn(Fixture.questionCategory());

        // When
        sut.saveQuestionCategory(dto);

        // Then
        then(questionCategoryRepository).should().save(any(QuestionCategory.class));
    }

    @DisplayName("UPDATE - 카테고리 수정")
    @Test
    public void 카테고리_수정_성공() {

        // Given
        QuestionCategory questionCategory = Fixture.questionCategory();
        QuestionCategoryModifyRequestDto updateRequestDto = Fixture.questionCategoryModifyRequestDto();
        given(questionCategoryRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(questionCategory));

        // When
        sut.updateQuestionCategory(updateRequestDto);

        // Then
        assertThat(questionCategory).hasFieldOrPropertyWithValue("categoryName", updateRequestDto.getCategoryName());
        then(questionCategoryRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - 카테고리 수정 - 실패(존재하지 않는 카테고리)")
    @Test
    public void 카테고리_수정_실패() {

        // Given
        QuestionCategoryModifyRequestDto updateRequestDto = Fixture.questionCategoryModifyRequestDto();
        given(questionCategoryRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.updateQuestionCategory(updateRequestDto);
        });
        then(questionCategoryRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - 카테고리 삭제")
    @Test
    public void 카테고리_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(questionCategoryRepository).deleteById(id);

        // When
        sut.deleteQuestionCategory(id);

        // Then
        then(questionCategoryRepository).should().deleteById(id);

    }

}