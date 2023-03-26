package com.dateplanner.support.dto;

import com.dateplanner.admin.consumer.entity.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionDto {

    @Schema(description = "문의 ID")
    private Long id;

    @Schema(description = "문의 유저 ID")
    private Long uid;

    @Schema(description = "문의 유저 닉네임")
    private String nickname;

    @Schema(description = "문의 유저 이메일")
    private String email;

    @Schema(description = "문의 카테고리 ID, 1(장소 관련), 2(계정 관련), 3(이벤트 관련), 4(오류 관련), 5(기타)")
    private Long categoryId;

    @Schema(description = "문의 카테고리명")
    private String categoryName;

    @Schema(description = "문의 제목")
    private String title;

    @Schema(description = "문의 내용")
    private String description;

    @Schema(description = "문의 내 답변 리스트")
    private List<AnswerDto> answers;

    @Schema(description = "문의 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema(description = "문의 수정 시간")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    public static QuestionDto from(Question entity) {
        return QuestionDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .nickname(entity.getUser().getNickname())
                .email(entity.getUser().getEmail())
                .categoryId(entity.getQuestionCategory().getId())
                .categoryName(entity.getQuestionCategory().getCategoryName())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .answers(entity.getAnswers().stream().map(AnswerDto::from).collect(Collectors.toList()))
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
