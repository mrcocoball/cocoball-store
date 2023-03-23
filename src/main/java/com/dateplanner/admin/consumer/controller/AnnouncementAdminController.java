package com.dateplanner.admin.consumer.controller;

import com.dateplanner.admin.consumer.dto.*;
import com.dateplanner.admin.consumer.service.AnnouncementService;
import com.dateplanner.common.pagination.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/service/announcements")
public class AnnouncementAdminController {

    private final AnnouncementService announcementService;
    private final PaginationService paginationService;


    /**
     * 공지사항 관련
     */

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAnnouncementList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      ModelMap map) {

        Page<AnnouncementDto> dtos = paginationService.listToPage(announcementService.getAnnouncementList(), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/service/announcements/announcements";

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAnnouncement(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementDto dto = announcementService.getAnnouncement(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_detail";

    }

    @GetMapping("/write")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getWriteForm(ModelMap map) {

        List<AnnouncementCategoryDto> categories = announcementService.getAnnouncementCategoryList();
        map.addAttribute("categories", categories);

        return "admin/service/announcements/announcements_write";
    }

    @PostMapping("/write")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String writeAnnouncement(@Valid AnnouncementRequestDto dto,
                                    BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/write";

        }

        announcementService.saveAnnouncement(dto);

        return "redirect:/admin/service/announcements";

    }

    @GetMapping("/{id}/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getModifyForm(@PathVariable("id") Long id, ModelMap map) {

        List<AnnouncementCategoryDto> categories = announcementService.getAnnouncementCategoryList();
        AnnouncementModifyRequestDto dto = AnnouncementModifyRequestDto.from(announcementService.getAnnouncement(id));
        map.addAttribute("dto", dto);
        map.addAttribute("categories", categories);

        return "admin/service/announcements/announcements_modify";

    }

    @PostMapping("/{id}/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String modifyAnnouncement(@PathVariable("id") Long id,
                                     @Valid AnnouncementModifyRequestDto dto,
                                     BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController modifyAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/" + id + "/modify";

        }

        announcementService.updateAnnouncement(dto);

        return "redirect:/admin/service/announcements/" + id;

    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAnnouncement(@PathVariable("id") Long id) {

        announcementService.deleteAnnouncement(id);

        return "redirect:/admin/service/announcements";

    }


    /**
     * 공지사항 카테고리 관련
     */

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAnnouncementCategoryList(ModelMap map) {

        List<AnnouncementCategoryDto> dtos = announcementService.getAnnouncementCategoryList();
        map.addAttribute("dtos", dtos);

        return "admin/service/announcements/announcements_category_list";

    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAnnouncementCategory(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementCategoryDto dto = announcementService.getAnnouncementCategory(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_category_detail";

    }

    @GetMapping("/categories/write")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCategoryWriteForm() {

        return "admin/service/announcements/announcements_category_write";
    }

    @PostMapping("/categories/write")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String writeAnnouncementCategory(@Valid AnnouncementCategoryRequestDto dto,
                                            BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncementCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/categories/write";

        }

        announcementService.saveAnnouncementCategory(dto);

        return "redirect:/admin/service/announcements/categories";

    }

    @GetMapping("/categories/{id}/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCategoryModifyForm(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementCategoryModifyRequestDto dto = AnnouncementCategoryModifyRequestDto.from(announcementService.getAnnouncementCategory(id));
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_category_modify";

    }

    @PostMapping("/categories/{id}/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String modifyAnnouncementCategory(@PathVariable("id") Long id,
                                             @Valid AnnouncementCategoryModifyRequestDto dto,
                                             BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController modifyAnnouncementCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/categories/" + id + "/modify";

        }

        announcementService.updateAnnouncementCategory(dto);

        return "redirect:/admin/service/announcements/categories/" + id;

    }

    @PostMapping("/categories/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteAnnouncementCategory(@PathVariable("id") Long id) {

        announcementService.deleteAnnouncementCategory(id);

        return "redirect:/admin/service/announcements/categories";

    }


}
