package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {

    private Long id;

    private Long uid;

    private String username;

    private String email;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String description;

    private List<AnswerDto> answers;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static QuestionDto from(Question entity) {
        return QuestionDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .username(entity.getUser().getUsername())
                .email(entity.getUser().getEmail())
                .categoryId(entity.getQuestionCategory().getId())
                .categoryName(entity.getQuestionCategory().getCategoryName())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .answers(entity.getAnswers().stream().map(AnswerDto::from).collect(Collectors.toList()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
