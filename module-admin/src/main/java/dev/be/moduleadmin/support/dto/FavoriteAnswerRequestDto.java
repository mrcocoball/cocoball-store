package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.FavoriteAnswer;
import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
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
public class FavoriteAnswerRequestDto {

    private Long categoryId;

    @NotNull(message = "제목이 입력되어야 합니다")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    private String title;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String description;

    public FavoriteAnswer toEntity(String title, String description, FavoriteQuestionCategory favoriteQuestionCategory) {
        return FavoriteAnswer.of(title, description, favoriteQuestionCategory);
    }

}
