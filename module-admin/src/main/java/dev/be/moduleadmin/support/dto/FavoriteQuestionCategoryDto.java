package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteQuestionCategoryDto {

    private Long id;

    private String categoryName;

    private List<FavoriteAnswerDto> favoriteAnswers;

    public static FavoriteQuestionCategoryDto from(FavoriteQuestionCategory entity) {
        return FavoriteQuestionCategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .favoriteAnswers(entity.getFavoriteAnswers().stream().map(FavoriteAnswerDto::from).collect(Collectors.toList()))
                .build();
    }

}
