package com.dateplanner.admin.place.controller;

import com.dateplanner.admin.place.dto.PlaceAdminDetailDto;
import com.dateplanner.admin.place.dto.PlaceModifyRequestDto;
import com.dateplanner.admin.place.dto.PlaceRequestDto;
import com.dateplanner.admin.place.service.PlaceAdminService;
import com.dateplanner.common.pagination.PaginationService;
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
@RequestMapping("/admin/places")
public class PlaceAdminController {

    private final PlaceAdminService placeAdminService;
    private final PaginationService paginationService;

    @GetMapping()
    public String getPlaceList(@RequestParam(required = false) String region1, @RequestParam(required = false) String region2, @RequestParam(required = false) String region3,
                               @RequestParam(required = false) String categoryId, @RequestParam(required = false) Long id,
                               @RequestParam(required = false) String placeId, @RequestParam(required = false) String placeName, @RequestParam(required = false) Long reviewCount,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
                               @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, ModelMap map) {

        Page<PlaceAdminDetailDto> dtos = paginationService.listToPage(
                placeAdminService.getPlaceList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate), pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/places/places";
    }

    @GetMapping("/{id}")
    public String getPlace(@PathVariable("id") Long id, ModelMap map) {

        PlaceAdminDetailDto dto = placeAdminService.getPlace(id);
        map.addAttribute("dto", dto);

        return "admin/places/places_detail";
    }

    @GetMapping("/create")
    public String createPlaceForm() {

        return "admin/places/places_create";

    }

    @PostMapping("/create")
    public String createPlace(@Valid PlaceRequestDto dto,
                             BindingResult r,
                             RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[PlaceAdminController createPlace] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/places/create";

        }

        placeAdminService.savePlace(dto);

        return "redirect:/admin/places";

    }

    @GetMapping("/{id}/modify")
    public String modifyPlaceForm(@PathVariable("id") Long id, ModelMap map) {

        PlaceModifyRequestDto dto = PlaceModifyRequestDto.from(placeAdminService.getPlace(id));
        map.addAttribute("dto", dto);

        return "admin/places/places_modify";

    }

    @PostMapping("/{id}/modify")
    public String modifyPlace(@PathVariable("id") Long id,
                             @Valid PlaceModifyRequestDto dto,
                             BindingResult r,
                             RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[PlaceAdminController modifyPlace] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/places/" + id + "/modify";

        }

        placeAdminService.updatePlace(dto);

        return "redirect:/admin/places/" + id;

    }

    @PostMapping("/{id}/delete")
    public void deletePlace(@PathVariable("id") Long id) {

        placeAdminService.deletePlace(id);

    }
}


