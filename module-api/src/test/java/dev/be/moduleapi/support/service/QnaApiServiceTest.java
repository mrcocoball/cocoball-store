package dev.be.moduleapi.support.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.support.dto.QuestionDto;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.repositories.support.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[단일] QNA 화면 처리 서비스 - QNA 조회 테스트")
@ExtendWith(MockitoExtension.class)
class QnaApiServiceTest {

    @InjectMocks
    private QnaApiService sut;

    @Mock
    private QuestionRepository questionRepository;

    @DisplayName("READ - QNA 리스트 조회")
    @Test
    public void QNA_리스트_조회() {

        // Given
        String nickname = "test";
        Pageable pageable = Pageable.ofSize(10);
        given(questionRepository.findByUser_Nickname(nickname, pageable)).willReturn(Page.empty());

        // When
        Page<QuestionDto> result = sut.getQuestions(nickname, pageable);

        // Then
        assertThat(result).isEmpty();
        then(questionRepository).should().findByUser_Nickname(nickname, pageable);

    }

    @DisplayName("READ - QNA 단건 조회")
    @Test
    public void QNA_단건_조회_성공() {

        // Given
        Long id = 1L;
        Question question = Fixture.question();
        given(questionRepository.findById(id)).willReturn(Optional.of(question));

        // When
        QuestionDto dto = sut.getQuestion(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", question.getTitle())
                .hasFieldOrPropertyWithValue("description", question.getDescription());
        then(questionRepository).should().findById(id);

    }

    @DisplayName("READ - QNA 단건 조회 - 실패(존재하지 않는 질문)")
    @Test
    public void QNA_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(questionRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            sut.getQuestion(id);
        });
        then(questionRepository).should().findById(id);

    }

}