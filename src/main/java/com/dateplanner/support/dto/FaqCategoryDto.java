package com.dateplanner.support.dto;

import com.dateplanner.admin.consumer.entity.FavoriteQuestionCategory;
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
public class FaqCategoryDto {

    private Long id;

    private String categoryName;

    private List<FaqDto> favoriteAnswers;

    public static FaqCategoryDto from(FavoriteQuestionCategory entity) {
        return FaqCategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .favoriteAnswers(entity.getFavoriteAnswers().stream().map(FaqDto::from).collect(Collectors.toList()))
                .build();
    }


}
