package com.dateplanner.admin.user.controller;

import com.dateplanner.admin.user.dto.UserModifyRequestDto;
import com.dateplanner.admin.user.dto.UserRequestDto;
import com.dateplanner.admin.user.dto.UserResponseDto;
import com.dateplanner.admin.user.service.UserAdminService;
import com.dateplanner.common.pagination.PaginationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/users")
public class UserAdminController {


    private final UserAdminService userAdminService;
    private final PaginationService paginationService;


    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/id/{id}")
    public String getUserByUid(@PathVariable("id") Long uid, ModelMap map) {

        UserResponseDto dto = userAdminService.getUserByUid(uid);
        map.addAttribute("dto", dto);

        return "admin/users/users_detail";
    }


    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createUserForm() {

        return "admin/users/users_create";

    }

    // @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createUser(@Valid UserRequestDto dto,
                             BindingResult r,
                             RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[UserAdminController createUser] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/users/create";

        }

        userAdminService.saveUser(dto);

        return "redirect:/admin/users";

    }

    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String modifyUserForm(@PathVariable("id") Long uid, ModelMap map) {

        UserModifyRequestDto dto = UserModifyRequestDto.from(userAdminService.getUserByUid(uid));
        map.addAttribute("dto", dto);

        return "admin/users/users_modify";

    }

    // @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modifyUser(@PathVariable("id") Long uid,
                             @Valid UserModifyRequestDto dto,
                             BindingResult r,
                             RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[UserAdminController modifyUser] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/users/" + uid + "/modify";

        }

        userAdminService.updateUser(dto);

        return "redirect:/admin/users/" + uid;

    }

    // @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public void deleteUser(@Parameter(description = "회원 ID", required = true) @PathVariable("id") Long uid) {

        userAdminService.deleteUser(uid);

    }
}
