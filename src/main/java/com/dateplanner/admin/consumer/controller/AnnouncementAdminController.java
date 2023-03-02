package com.dateplanner.admin.consumer.controller;

import com.dateplanner.admin.consumer.dto.AnnouncementDto;
import com.dateplanner.admin.consumer.dto.AnnouncementModifyRequestDto;
import com.dateplanner.admin.consumer.dto.AnnouncementRequestDto;
import com.dateplanner.admin.consumer.service.AnnouncementService;
import com.dateplanner.common.pagination.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class AnnouncementAdminController {

    private final AnnouncementService announcementService;
    private final PaginationService paginationService;


    @GetMapping("/admin/service/announcements")
    public String getAnnouncementList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      ModelMap map) {

        Page<AnnouncementDto> dtos = paginationService.listToPage(announcementService.getAnnouncementList(), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/service/announcements";

    }

    @GetMapping("/admin/service/announcements/{id}")
    public String getAnnouncement(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementDto dto = announcementService.getAnnouncement(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements_detail";

    }

    @GetMapping("/admin/service/announcements/write")
    public String getWriteForm() {

        return "admin/service/announcements_write";
    }

    @PostMapping("/admin/service/announcements/write")
    public String writeAnnouncement(@Valid AnnouncementRequestDto dto, BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/write";

        }

        announcementService.saveAnnouncement(dto);

        return "redirect:/admin/service/announcements";

    }

    @GetMapping("/admin/service/announcements/{id}/modify")
    public String getModifyForm(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementModifyRequestDto dto = AnnouncementModifyRequestDto.from(announcementService.getAnnouncement(id));
        map.addAttribute("dto", dto);

        return "admin/service/announcements_modify";

    }

    @PostMapping("/admin/service/announcements/{id}/modify")
    public String modifyAnnouncement(@Valid AnnouncementModifyRequestDto dto, BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/modify";

        }

        announcementService.updateAnnouncement(dto);

        return "redirect:/admin/service/announcements";

    }

    @PostMapping("/admin/service/announcements/{id}/delete")
    public String deleteAnnouncement(@PathVariable("id") Long id) {

        announcementService.deleteAnnouncement(id);

        return "redirect:/admin/service/announcements";

    }


}
