package com.dateplanner.security.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

}
