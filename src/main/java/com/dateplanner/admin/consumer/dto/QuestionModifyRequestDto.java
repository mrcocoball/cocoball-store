package com.dateplanner.admin.consumer.dto;

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
public class QuestionModifyRequestDto {

    private Long id;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String title;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String description;

    private Long categoryId;

    private String username;

    public static QuestionModifyRequestDto from(QuestionDto dto) {

        return QuestionModifyRequestDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .username(dto.getUsername())
                .build();

    }

}
