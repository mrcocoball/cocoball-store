package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionCategoryDto {

    private Long id;

    private String categoryName;

    public static QuestionCategoryDto from(QuestionCategory entity) {
        return QuestionCategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .build();
    }

}
