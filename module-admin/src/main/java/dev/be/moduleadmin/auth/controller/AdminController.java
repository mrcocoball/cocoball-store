package dev.be.moduleadmin.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
public class AdminController {

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String index() {
		return "admin/index";
    }

    @GetMapping("/user/login")
    public String loginGet() {
		return "admin/login";
    }


}
