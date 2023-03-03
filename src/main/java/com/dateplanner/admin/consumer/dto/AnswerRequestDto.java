package com.dateplanner.admin.consumer.dto;

import com.dateplanner.admin.consumer.entity.Answer;
import com.dateplanner.admin.consumer.entity.Question;
import com.dateplanner.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDto {

    private String username;

    private Long qid;

    private Long id;

    private String description;

    public Answer toEntity(User user, Question question, String description) {

        return Answer.of(
                user,
                question,
                description
        );
    }

}
