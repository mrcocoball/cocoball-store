package com.dateplanner.admin.user.dto;

import com.dateplanner.admin.user.entity.UserPasswordRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPasswordRequestDto {

    private Long id;

    private Long uid;

    private String email;

    private boolean complete;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static UserPasswordRequestDto from(UserPasswordRequest entity) {
        return UserPasswordRequestDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .email(entity.getUser().getEmail())
                .complete(entity.isComplete())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
