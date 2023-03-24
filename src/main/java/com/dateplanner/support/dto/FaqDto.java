package com.dateplanner.support.dto;

import com.dateplanner.admin.consumer.entity.FavoriteAnswer;
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
public class FaqDto {

    private Long id;

    private Long categoryId;

    private String title;

    private String description;

    public static FaqDto from(FavoriteAnswer entity) {
        return FaqDto.builder()
                .id(entity.getId())
                .categoryId(entity.getFavoriteQuestionCategory().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

}
