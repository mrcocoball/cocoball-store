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
@RequestMapping("/admin/service/announcements")
public class AnnouncementAdminController {

    private final AnnouncementService announcementService;
    private final PaginationService paginationService;


    @GetMapping()
    public String getAnnouncementList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      ModelMap map) {

        Page<AnnouncementDto> dtos = paginationService.listToPage(announcementService.getAnnouncementList(), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/service/announcements";

    }

    @GetMapping("/{id}")
    public String getAnnouncement(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementDto dto = announcementService.getAnnouncement(id);
        map.addAttribute("dto", dto);

        return "admin/service/announcements_detail";

    }

    @GetMapping("/write")
    public String getWriteForm() {

        return "admin/service/announcements_write";
    }

    @PostMapping("/write")
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
    public String getModifyForm(@PathVariable("id") Long id, ModelMap map) {

        AnnouncementModifyRequestDto dto = AnnouncementModifyRequestDto.from(announcementService.getAnnouncement(id));
        map.addAttribute("dto", dto);

        return "admin/service/announcements_modify";

    }

    @PostMapping("/{id}/modify")
    public String modifyAnnouncement(@PathVariable("id") Long id,
                                     @Valid AnnouncementModifyRequestDto dto,
                                     BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[AnnounceAdminController writeAnnouncement] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/announcements/" + id + "/modify";

        }

        announcementService.updateAnnouncement(dto);

        return "redirect:/admin/service/announcements/" + id;

    }

    @PostMapping("/{id}/delete")
    public String deleteAnnouncement(@PathVariable("id") Long id) {

        announcementService.deleteAnnouncement(id);

        return "redirect:/admin/service/announcements";

    }


}
