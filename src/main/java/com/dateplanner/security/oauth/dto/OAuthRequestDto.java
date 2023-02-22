package com.dateplanner.security.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;

@Getter
@AllArgsConstructor
public class OAuthRequestDto {

    @Schema(description = "토큰을 발급받는 url")
    private String url;

    @Schema(description = "액세스 토큰 요청 시 x-ww-form-urlencoded를 요구할 때 변환")
    private LinkedMultiValueMap<String, String> map;

}
