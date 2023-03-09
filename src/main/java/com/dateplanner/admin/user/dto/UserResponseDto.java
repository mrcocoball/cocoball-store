package com.dateplanner.admin.user.dto;

import com.dateplanner.user.entity.User;
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
public class UserResponseDto {

    private Long uid;
    private String email;
    private String nickname;
    private boolean social;
    private boolean deleted;
    private String provider;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public static UserResponseDto from(User entity) {
        return UserResponseDto.builder()
                .uid(entity.getUid())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .social(entity.isSocial())
                .deleted(entity.isDeleted())
                .provider(entity.getProvider())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
