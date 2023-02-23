package com.dateplanner.security.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthAccessTokenDto {

    /**
     * 인가 코드를 통해 카카오 측으로 얻은 카카오 토큰
     * 해당 코튼의 access_token, refresh_token을 통해 소셜 로그인 / 소셜 회원가입을 진행한다.
     * gson을 활용해야 하므로 객체의 프로퍼티는 JSON의 key-value의 이름과 동일하게 작성
     */

    @Schema(description = "액세스 토큰")
    private String access_token;

    @Schema(description = "토큰 타입, bearer")
    private String token_type;

    @Schema(description = "리프레시 토큰")
    private String refresh_token;

    @Schema(description = "액세스 토큰 만료일")
    private Long expires_in;

    @Schema(description = "리프레시 토큰 만료일")
    private Long refresh_token_expires_in;

}
