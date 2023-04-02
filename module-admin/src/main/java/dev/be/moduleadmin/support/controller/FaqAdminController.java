package dev.be.moduleadmin.support.controller;

import dev.be.moduleadmin.support.dto.*;
import dev.be.moduleadmin.support.service.FaqAdminService;
import dev.be.modulecore.service.PaginationService;
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
import java.util.List;

@Slf4j(topic = "CONTROLLER")
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/service/faq")
public class FaqAdminController {

    private final FaqAdminService faqAdminService;
    private final PaginationService paginationService;

    /**
     * 카테고리 관련
     */

    @GetMapping
    public String getFavoriteQuestionCategoryList(ModelMap map) {

        List<FavoriteQuestionCategoryDto> dtos = faqAdminService.getFavoriteQuestionCategoryList();
        map.addAttribute("dtos", dtos);

        return "admin/service/faq/faq";
    }

    @GetMapping("/{id}")
    public String getFavoriteQuestionCategory(@PathVariable("id") Long id, ModelMap map) {

        FavoriteQuestionCategoryDto dto = faqAdminService.getFavoriteQuestionCategory(id);
        map.addAttribute("dto", dto);

        return "admin/service/faq/faq_detail";

    }

    @GetMapping("/write")
    public String getCategoryWriteForm() {

        return "admin/service/faq/faq_write";
    }

    @PostMapping("/write")
    public String writeFavoriteQuestionCategory(@Valid FavoriteQuestionCategoryRequestDto dto, BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[FaqAdminController writeFavoriteQuestionCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/faq/write";

        }

        faqAdminService.saveFavoriteQuestionCategory(dto);

        return "redirect:/admin/service/faq";
    }

    @GetMapping("/{id}/modify")
    public String getCategoryModifyForm(@PathVariable("id") Long id, ModelMap map) {

        FavoriteQuestionCategoryModifyRequestDto dto = FavoriteQuestionCategoryModifyRequestDto.from(faqAdminService.getFavoriteQuestionCategory(id));
        map.addAttribute("dto", dto);

        return "admin/service/faq/faq_modify";
    }

    @PostMapping("/{id}/modify")
    public String modifyFavoriteQuestionCategory(@PathVariable("id") Long id,
                                                 @Valid FavoriteQuestionCategoryModifyRequestDto dto,
                                                 BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[FaqAdminController modifyFavoriteQuestionCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/faq/" + id + "/modify";

        }

        faqAdminService.updateFavoriteQuestionCategory(dto);

        return "redirect:/admin/service/faq/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteFavoriteQuestionCategory(@PathVariable("id") Long id) {

        faqAdminService.deleteFavoriteQuestionCategory(id);

        return "redirect:/admin/service/faq";

    }


    /**
     * 카테고리 별 FAQ 관련
     */

    @GetMapping("/answers/{id}")
    public String getFavoriteAnswer(@PathVariable("id") Long id, ModelMap map) {

        FavoriteAnswerDto dto = faqAdminService.getFavoriteAnswer(id);
        map.addAttribute("dto", dto);

        return "admin/service/faq/faq_answer_detail";

    }

    @GetMapping("/answers/write")
    public String getAnswerWriteForm(ModelMap map) {

        List<FavoriteQuestionCategoryDto> categories = faqAdminService.getFavoriteQuestionCategoryList();
        map.addAttribute("categories", categories);

        return "admin/service/faq/faq_answer_write";
    }

    @PostMapping("/answers/write")
    public String writeFavoriteAnswer(@Valid FavoriteAnswerRequestDto dto, BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[FaqAdminController writeFavoriteAnswer] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/faq/answers/write";

        }

        faqAdminService.saveFavoriteAnswer(dto);

        return "redirect:/admin/service/faq";
    }

    @GetMapping("/answers/{id}/modify")
    public String getAnswerModifyForm(@PathVariable("id") Long id, ModelMap map) {

        FavoriteAnswerModifyRequestDto dto = FavoriteAnswerModifyRequestDto.from(faqAdminService.getFavoriteAnswer(id));
        map.addAttribute("dto", dto);

        return "admin/service/faq/faq_answer_modify";
    }

    @PostMapping("/answers/{id}/modify")
    public String modifyFavoriteAnswer(@PathVariable("id") Long id,
                                       @Valid FavoriteAnswerModifyRequestDto dto,
                                       BindingResult r, RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[FaqAdminController modifyFavoriteAnswer] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/faq/answers/" + id + "/modify";

        }

        faqAdminService.updateFavoriteAnswer(dto);

        return "redirect:/admin/service/faq/answers/" + id;
    }

    @PostMapping("/answers/{id}/delete")
    public String deleteFavoriteAnswer(@PathVariable("id") Long id) {

        faqAdminService.deleteFavoriteAnswer(id);

        return "redirect:/admin/service/faq";

    }

}
