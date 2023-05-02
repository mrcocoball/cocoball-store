package dev.be.moduleapi.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @Schema(description = "토큰 타입, Bearer 사용")
    private String grantType;

    @Schema(description = "액세스 토큰")
    @JsonProperty("Authorization")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "액세스 토큰 만료일")
    private Long accessTokenExpireDate;

    @Schema(description = "유저 ID")
    private Long uid;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "소셜 계정 유무")
    private boolean isSocial;

}
