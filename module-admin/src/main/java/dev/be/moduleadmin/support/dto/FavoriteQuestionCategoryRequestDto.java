package dev.be.moduleadmin.support.dto;

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
public class FavoriteQuestionCategoryRequestDto {

    @NotNull(message = "제목이 입력되어야 합니다")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    private String categoryName;

    public FavoriteQuestionCategory toEntity(String categoryName) {

        return FavoriteQuestionCategory.of(categoryName);

    }

}
