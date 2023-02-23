package com.dateplanner.security.oauth.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoProfile {

    private KakaoAccount kakao_account;
    private Properties properties;

    @Data
    public class KakaoAccount {
        private String email;
    }

    @Data
    public class Properties {
        private String nickname;
    }

}
