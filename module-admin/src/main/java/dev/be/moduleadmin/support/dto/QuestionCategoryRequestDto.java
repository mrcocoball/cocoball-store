package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.QuestionCategory;
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
public class QuestionCategoryRequestDto {

    @NotNull(message = "제목이 입력되어야 합니다")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    private String categoryName;

    public QuestionCategory toEntity(String categoryName) {

        return QuestionCategory.of(categoryName);

    }

}
