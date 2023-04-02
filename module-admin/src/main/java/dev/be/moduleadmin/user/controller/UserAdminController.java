package dev.be.moduleadmin.user.controller;

import dev.be.moduleadmin.user.dto.UserModifyRequestDto;
import dev.be.moduleadmin.user.dto.UserPasswordRequestDto;
import dev.be.moduleadmin.user.dto.UserRequestDto;
import dev.be.moduleadmin.user.dto.UserResponseDto;
import dev.be.moduleadmin.user.service.UserAdminService;
import dev.be.modulecore.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserAdminService userAdminService;
    private final PaginationService paginationService;


    @GetMapping()
    public String getUserList(@RequestParam(required = false) String email,
                              @RequestParam(required = false) String nickname,
                              @RequestParam(required = false) boolean deleted,
                              @RequestParam(required = false) boolean social,
                              @RequestParam(required = false) String provider,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                              @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                              ModelMap map) {


        Page<UserResponseDto> dtos = paginationService.listToPage(
                userAdminService.getUserList(email, nickname, deleted, social, provider, startDate, targetDate), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/users/users";
    }

    @GetMapping("/deleted")
    public String getDeletedUserList(@RequestParam(required = false) String email,
                                     @RequestParam(required = false) String nickname,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                                     @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                     ModelMap map) {


        Page<UserResponseDto> dtos = paginationService.listToPage(
                userAdminService.getDeletedUserList(email, nickname, startDate, targetDate), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/users/users_delete";
    }


    @GetMapping("/{uid}")
    public String getUserByUid(@PathVariable("uid") Long uid, ModelMap map) {

        UserResponseDto dto = userAdminService.getUserByUid(uid);
        map.addAttribute("dto", dto);

        return "admin/users/users_detail";
    }


    @GetMapping("/create")
    public String createUserForm() {

        return "admin/users/users_create";

    }

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

    @GetMapping("/{id}/modify")
    public String modifyUserForm(@PathVariable("id") Long uid, ModelMap map) {

        UserModifyRequestDto dto = UserModifyRequestDto.from(userAdminService.getUserByUid(uid));
        map.addAttribute("dto", dto);

        return "admin/users/users_modify";

    }

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

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long uid) {

        userAdminService.deleteUser(uid);

        return "redirect:/admin/users";

    }


    /**
     * 비밀번호 변경 요청 관련
     */

    @GetMapping("/passwordRequests")
    public String getUserPasswordRequestList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                             ModelMap map) {

        Page<UserPasswordRequestDto> dtos = paginationService.listToPage(userAdminService.getUserPasswordRequestList(), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/users/users_password";
    }

    // TODO : 실제 운영 직전에 비밀번호 변경 기능 추가할 것

}
