package dev.be.moduleapi.support.dto;

import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.support.QuestionCategory;
import dev.be.modulecore.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionRequestDto {

    @Schema(description = "문의 ID, 문의 수정 시에만 사용")
    private Long id;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    @Schema(description = "문의 제목")
    private String title;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    @Schema(description = "문의 내용")
    private String description;

    @Schema(description = "문의 카테고리 ID, 1(장소 관련), 2(계정 관련), 3(이벤트 관련), 4(오류 관련), 5(기타)")
    private Long categoryId;

    @Schema(description = "유저 닉네임")
    private String nickname;

    public Question toEntity(String title, String description, User user, QuestionCategory questionCategory) {

        return Question.of(
                title,
                description,
                user,
                questionCategory
        );

    }

}
