package com.dateplanner.admin.user.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyRequestDto {

    /**
     * 회원 수정용 Dto
     */

    @NotNull
    private Long uid;

    @NotNull
    @NotEmpty
    private String nickname;

    public static UserModifyRequestDto from(UserResponseDto dto) {
        return UserModifyRequestDto.builder()
                .uid(dto.getId())
                .nickname(dto.getNickname())
                .build();
    }

}
