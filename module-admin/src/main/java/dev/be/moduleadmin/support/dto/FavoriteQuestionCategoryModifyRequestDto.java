package dev.be.moduleadmin.support.dto;

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
public class FavoriteQuestionCategoryModifyRequestDto {

    private Long id;

    @NotNull(message = "제목이 입력되어야 합니다")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    private String categoryName;

    public static FavoriteQuestionCategoryModifyRequestDto from(FavoriteQuestionCategoryDto dto) {

        return FavoriteQuestionCategoryModifyRequestDto.builder()
                .id(dto.getId())
                .categoryName(dto.getCategoryName())
                .build();

    }

}
