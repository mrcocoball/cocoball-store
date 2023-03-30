package dev.be.moduleapi.support.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.be.modulecore.domain.support.FavoriteQuestionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqCategoryDto {

    @Schema(description = "FAQ 질문 카테고리 ID")
    private Long id;

    @Schema(description = "FAQ 질문 카테고리명")
    private String categoryName;

    @Schema(description = "FAQ 질문 카테고리 내 답변 리스트")
    private List<FaqDto> favoriteAnswers;

    @Schema(description = "작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    public static FaqCategoryDto from(FavoriteQuestionCategory entity) {
        return FaqCategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .favoriteAnswers(entity.getFavoriteAnswers().stream().map(FaqDto::from).collect(Collectors.toList()))
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }


}
