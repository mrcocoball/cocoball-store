package com.dateplanner.security.oauth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "0. [로그인 화면 - 소셜 로그인] UserOAuthController - 소셜 로그인 테스트용 API")
@RequiredArgsConstructor
@RestController
public class UserOAuthController {

    private final Environment env;

    @Value("${url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;


    /**
     * 소셜 로그인 관련 백엔드 서버에서 테스트를 하기 위해 만든 컨트롤러입니다.
     * /social/login 으로 접근하여 loginUrlKakao 버튼을 눌러 카카오 측으로 소셜 로그인 기능 인가 코드를 받습니다.
     * 인가 코드는 redirect를 통해 전달되며 이를 view에 표시하였습니다.
     * 해당 인가 코드를 소셜 로그인 / 회원가입 api에 활용합니다.
     */


    // 카카오 소셜 로그인 테스트
    @GetMapping("/social/login")
    public ModelAndView socialLogin(ModelAndView mv) {

        StringBuilder loginUrlKakao = new StringBuilder()
                .append(env.getProperty("social.kakao.url.login"))
                .append("?response_type=code")
                .append("&client_id=").append(kakaoClientId)
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectUri);
        mv.addObject("loginUrlKakao", loginUrlKakao);
        mv.setViewName("login");

        return mv;
    }

    // 인증 완료 후 리다이렉트 페이지
    @GetMapping("/social/login/{provider}")
    public ModelAndView redirect(ModelAndView mv, @RequestParam String code, @PathVariable("provider") String provider) {

        mv.addObject("code", code);
        mv.setViewName("redirect");

        return mv;
    }

}
