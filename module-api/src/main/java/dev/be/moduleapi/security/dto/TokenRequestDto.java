package dev.be.moduleapi.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequestDto {

    @Schema(description = "액세스 토큰")
    String accessToken;

    @Schema(description = "리프레시 토큰")
    String refreshToken;

}
