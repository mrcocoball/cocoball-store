package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.Answer;
import dev.be.modulecore.domain.support.Question;
import dev.be.modulecore.domain.user.User;
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
