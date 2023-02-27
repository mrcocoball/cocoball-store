package com.dateplanner.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSocialJoinRequestDto {

    @Schema(description = "인가 코드")
    private String code;

    @Schema(description = "인증 제공자")
    private String provider;

    @Schema(description = "사용자 닉네임")
    private String nickname;

}
