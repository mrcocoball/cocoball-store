package com.dateplanner.admin.consumer.dto;

import com.dateplanner.admin.consumer.entity.Question;
import com.dateplanner.admin.consumer.entity.QuestionCategory;
import com.dateplanner.user.entity.User;
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
public class QuestionRequestDto {

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String title;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String description;

    private Long categoryId;

    private String username;

    public Question toEntity(String title, String description, User user, QuestionCategory questionCategory) {

        return Question.of(
                title,
                description,
                user,
                questionCategory
        );

    }

}
