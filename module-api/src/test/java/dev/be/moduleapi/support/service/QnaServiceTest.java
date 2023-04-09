package dev.be.moduleapi.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.support.dto.AnswerRequestDto;
import dev.be.moduleapi.support.dto.QuestionRequestDto;
import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.AnswerRepository;
import dev.be.modulecore.repositories.support.QuestionCategoryRepository;
import dev.be.modulecore.repositories.support.QuestionRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] QNA 서비스 - QNA 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @InjectMocks
    private QnaService sut;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionCategoryRepository questionCategoryRepository;

    @Mock
    private AnswerRepository answerRepository;


    @DisplayName("CREATE - 질문 저장")
    @Test
    public void 질문_저장_성공() {

        // Given
        User user = Fixture.user();
        QuestionCategory questionCategory = Fixture.questionCategory();
        QuestionRequestDto dto = Fixture.questionRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(questionCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.of(questionCategory));
        given(questionRepository.save(any(Question.class))).willReturn(Fixture.question());

        // When
        sut.saveQuestion(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getNickname());
        then(questionCategoryRepository).should().findById(dto.getCategoryId());
        then(questionRepository).should().save(any(Question.class));
    }

    @DisplayName("CREATE - 질문 저장 - 실패(존재하지 않는 카테고리)")
    @Test
    public void 질문_저장_실패() {

        // Given
        User user = Fixture.user();
        QuestionRequestDto dto = Fixture.questionRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(questionCategoryRepository.findById(dto.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.saveQuestion(dto);
        });
        then(userRepository).should().findByNickname(dto.getNickname());
        then(questionCategoryRepository).should().findById(dto.getCategoryId());
    }

    @DisplayName("UPDATE - 질문 수정")
    @Test
    public void 질문_수정_성공() {

        // Given
        Question question = Fixture.question();
        QuestionRequestDto updateRequestDto = Fixture.questionUpdateRequestDto();
        given(questionRepository.findById(updateRequestDto.getId())).willReturn(Optional.of(question));

        // When
        sut.updateQuestion(updateRequestDto);

        // Then
        assertThat(question).hasFieldOrPropertyWithValue("title", updateRequestDto.getTitle());
        assertThat(question).hasFieldOrPropertyWithValue("description", updateRequestDto.getDescription());
        then(questionRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("UPDATE - 질문 수정 - 실패(존재하지 않는 질문)")
    @Test
    public void 질문_수정_실패() {

        // Given
        QuestionRequestDto updateRequestDto = Fixture.questionUpdateRequestDto();
        given(questionRepository.findById(updateRequestDto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.updateQuestion(updateRequestDto);
        });
        then(questionRepository).should().findById(updateRequestDto.getId());
    }

    @DisplayName("DELETE - 질문 삭제")
    @Test
    public void 질문_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(questionRepository).deleteById(id);

        // When
        sut.deleteQuestion(id);

        // Then
        then(questionRepository).should().deleteById(id);

    }

    @DisplayName("CREATE - 답변 저장")
    @Test
    public void 답변_저장_성공() {

        // Given
        User user = Fixture.user();
        Question question = Fixture.question();
        AnswerRequestDto dto = Fixture.answerRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(questionRepository.findById(dto.getQid())).willReturn(Optional.of(question));
        given(answerRepository.save(any(Answer.class))).willReturn(Fixture.answer());

        // When
        sut.saveAnswer(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getNickname());
        then(questionRepository).should().findById(dto.getQid());
        then(answerRepository).should().save(any(Answer.class));
    }

    @DisplayName("CREATE - 답변 저장 - 실패(존재하지 않는 질문)")
    @Test
    public void 답변_저장_실패() {

        // Given
        User user = Fixture.user();
        AnswerRequestDto dto = Fixture.answerRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(questionRepository.findById(dto.getQid())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.saveAnswer(dto);
        });
        then(userRepository).should().findByNickname(dto.getNickname());
        then(questionRepository).should().findById(dto.getQid());
    }

}