package com.dateplanner.admin.auth.controller;

import com.dateplanner.admin.auth.service.AdminJoinService;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.user.dto.UserLoginRequestDto;
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
    private final ResponseService responseService;

    @GetMapping("/login")
    public String loginGet() {
		return "admin/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public SingleResult<TokenDto> adminLogin(UserLoginRequestDto dto) {

        log.info("email = {}", dto.getEmail());

        TokenDto tokenDto = adminJoinService.adminLogin(dto);

        return responseService.getSingleResult(tokenDto);
    }

}
