package com.dateplanner.support.dto;

import com.dateplanner.admin.consumer.entity.Answer;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AnswerDto {

    private Long id;

    private Long uid;

    private String nickname;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public static AnswerDto from(Answer entity) {
        return AnswerDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .nickname(entity.getUser().getNickname())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
