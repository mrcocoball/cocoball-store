package dev.be.moduleadmin.support.controller;

import dev.be.moduleadmin.support.dto.*;
import dev.be.moduleadmin.support.service.AnnouncementAdminService;
import dev.be.modulecore.service.PaginationService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j(topic = "CONTROLLER")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/service/announcements")
public class AnnouncementAdminController {

    private final AnnouncementAdminService announcementAdminService;
    private final PaginationService paginationService;


    /**
     * 공지사항 관련
     */

    @GetMapping()
    public String getAnnouncementList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      ModelMap map) {

        Page<AnnouncementDto> dtos = paginationService.listToPage(announcementAdminService.getAnnouncementList(), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/service/announcements/announcements";

    }

    @GetMapping("/{id}")
    public String getAnnouncement(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementDto dto = announcementAdminService.getAnnouncement(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_detail";

    }

    @GetMapping("/write")
    public String getWriteForm(ModelMap map) {

        List<AnnouncementCategoryDto> categories = announcementAdminService.getAnnouncementCategoryList();
        map.addAttribute("categories", categories);

        return "admin/service/announcements/announcements_write";
    }

    @PostMapping("/write")
    public String writeAnnouncement(@Valid AnnouncementRequestDto dto,
                                    BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/write";

        }

        announcementAdminService.saveAnnouncement(dto);

        return "redirect:/admin/service/announcements";

    }

    @GetMapping("/{id}/modify")
    public String getModifyForm(@PathVariable("id") Long id, ModelMap map) {

        List<AnnouncementCategoryDto> categories = announcementAdminService.getAnnouncementCategoryList();
        AnnouncementModifyRequestDto dto = AnnouncementModifyRequestDto.from(announcementAdminService.getAnnouncement(id));
        map.addAttribute("dto", dto);
        map.addAttribute("categories", categories);

        return "admin/service/announcements/announcements_modify";

    }

    @PostMapping("/{id}/modify")
    public String modifyAnnouncement(@PathVariable("id") Long id,
                                     @Valid AnnouncementModifyRequestDto dto,
                                     BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController modifyAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/" + id + "/modify";

        }

        announcementAdminService.updateAnnouncement(dto);

        return "redirect:/admin/service/announcements/" + id;

    }

    @PostMapping("/{id}/delete")
    public String deleteAnnouncement(@PathVariable("id") Long id) {

        announcementAdminService.deleteAnnouncement(id);

        return "redirect:/admin/service/announcements";

    }


    /**
     * 공지사항 카테고리 관련
     */

    @GetMapping("/categories")
    public String getAnnouncementCategoryList(ModelMap map) {

        List<AnnouncementCategoryDto> dtos = announcementAdminService.getAnnouncementCategoryList();
        map.addAttribute("dtos", dtos);

        return "admin/service/announcements/announcements_category_list";

    }

    @GetMapping("/categories/{id}")
    public String getAnnouncementCategory(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementCategoryDto dto = announcementAdminService.getAnnouncementCategory(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_category_detail";

    }

    @GetMapping("/categories/write")
    public String getCategoryWriteForm() {

        return "admin/service/announcements/announcements_category_write";
    }

    @PostMapping("/categories/write")
    public String writeAnnouncementCategory(@Valid AnnouncementCategoryRequestDto dto,
                                            BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncementCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/categories/write";

        }

        announcementAdminService.saveAnnouncementCategory(dto);

        return "redirect:/admin/service/announcements/categories";

    }

    @GetMapping("/categories/{id}/modify")
    public String getCategoryModifyForm(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementCategoryModifyRequestDto dto = AnnouncementCategoryModifyRequestDto.from(announcementAdminService.getAnnouncementCategory(id));
        map.addAttribute("dto", dto);

        return "admin/service/announcements/announcements_category_modify";

    }

    @PostMapping("/categories/{id}/modify")
    public String modifyAnnouncementCategory(@PathVariable("id") Long id,
                                             @Valid AnnouncementCategoryModifyRequestDto dto,
                                             BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController modifyAnnouncementCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/categories/" + id + "/modify";

        }

        announcementAdminService.updateAnnouncementCategory(dto);

        return "redirect:/admin/service/announcements/categories/" + id;

    }

    @PostMapping("/categories/{id}/delete")
    public String deleteAnnouncementCategory(@PathVariable("id") Long id) {

        announcementAdminService.deleteAnnouncementCategory(id);

        return "redirect:/admin/service/announcements/categories";

    }


}
