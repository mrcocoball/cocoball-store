package dev.be.moduleapi.support.dto;

import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDto {

    @Schema(description = "답변 ID")
    private Long id;

    @Schema(description = "문의 ID, 문의 수정 시에만 사용")
    private String nickname;

    @Schema(description = "질문 ID")
    private Long qid;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    @Schema(description = "답변 내용")
    private String description;

    public Answer toEntity(User user, Question question, String description) {

        return Answer.of(
                user,
                question,
                description
        );
    }

}
