package dev.be.moduleadmin.auth.controller;

import dev.be.moduleadmin.auth.service.AdminJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminJoinService adminJoinService;

    @GetMapping("/login")
    public String loginGet() {
		return "admin/login";
    }

    // TODO :: 모듈 분리 이후 인증 코드 변경 필요

    /*
    @ResponseBody
    @PostMapping("/login")
    public SingleResult<TokenDto> adminLogin(UserLoginRequestDto dto) {

        log.info("email = {}", dto.getEmail());

        TokenDto tokenDto = adminJoinService.adminLogin(dto);

        return responseService.getSingleResult(tokenDto);
    }

     */

}
