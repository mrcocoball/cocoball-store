package dev.be.moduleapi.support.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.be.modulecore.domain.support.FavoriteAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqDto {

    @Schema(description = "FAQ 질문 카테고리 내 답변 ID")
    private Long id;

    @Schema(description = "FAQ 질문 카테고리 ID")
    private Long categoryId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String description;

    @Schema(description = "작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    public static FaqDto from(FavoriteAnswer entity) {
        return FaqDto.builder()
                .id(entity.getId())
                .categoryId(entity.getFavoriteQuestionCategory().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
