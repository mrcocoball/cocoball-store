package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.FavoriteAnswer;
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
public class FavoriteAnswerDto {

    private Long id;

    private Long categoryId;

    private String title;

    private String description;

    public static FavoriteAnswerDto from(FavoriteAnswer entity) {
        return FavoriteAnswerDto.builder()
                .id(entity.getId())
                .categoryId(entity.getFavoriteQuestionCategory().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

}
