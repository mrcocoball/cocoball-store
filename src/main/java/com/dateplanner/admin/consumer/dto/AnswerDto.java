package com.dateplanner.admin.consumer.dto;

import com.dateplanner.admin.consumer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

    private Long id;

    private Long uid;

    private String username;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static AnswerDto from(Answer entity) {
        return AnswerDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .username(entity.getUser().getUsername())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
