package dev.be.moduleadmin.support.controller;

import dev.be.moduleadmin.support.dto.*;
import dev.be.moduleadmin.support.service.QnaAdminService;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/admin/service/qna")
public class QnaAdminController {

    private final QnaAdminService qnaAdminService;
    private final PaginationService paginationService;

    /**
     * 질문 관련
     */

    @GetMapping
    public String getQuestionList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                  ModelMap map) {

        Page<QuestionDto> dtos = qnaAdminService.getQuestionList(pageable);
        List<Integer> pageBarNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), dtos.getTotalPages());
        map.addAttribute("dtos", dtos);
        map.addAttribute("pageBarNumbers", pageBarNumbers);

        return "admin/service/qna/qna";

    }

    @GetMapping("/{id}")
    public String getQuestion(@PathVariable("id") Long id, ModelMap map) {

        QuestionDto dto = qnaAdminService.getQuestion(id);
        map.addAttribute("dto", dto);

        return "admin/service/qna/qna_detail";

    }

    @GetMapping("/write")
    public String getWriteForm(ModelMap map) {

        List<QuestionCategoryDto> categories = qnaAdminService.getQuestionCategoryList();
        map.addAttribute("categories", categories);

        return "admin/service/qna/qna_write";

    }

    @PostMapping("/write")
    public String writeQuestion(@Valid QuestionRequestDto dto,
                                BindingResult r,
                                RedirectAttributes ra,
                                Authentication authentication) {

        if (r.hasErrors()) {

            log.info("[QnaAdminController writeQuestion] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/qna/write";

        }

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setUsername(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입

        qnaAdminService.saveQuestion(dto);

        return "redirect:/admin/service/qna";

    }

    @GetMapping("/{id}/modify")
    public String getModifyForm(@PathVariable("id") Long id, ModelMap map) {

        QuestionModifyRequestDto dto = QuestionModifyRequestDto.from(qnaAdminService.getQuestion(id));
        List<QuestionCategoryDto> categories = qnaAdminService.getQuestionCategoryList();
        map.addAttribute("dto", dto);
        map.addAttribute("categories", categories);

        return "admin/service/qna/qna_modify";

    }

    @PostMapping("/{id}/modify")
    public String modifyQuestion(@PathVariable("id") Long id,
                                 @Valid QuestionModifyRequestDto dto,
                                 BindingResult r,
                                 RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[QnaAdminController modifyQuestion] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/qna/" + id + "/modify";

        }

        qnaAdminService.updateQuestion(dto);

        return "redirect:/admin/service/qna/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteQuestion(@PathVariable("id") Long id) {

        qnaAdminService.deleteQuestion(id);

        return "redirect:/admin/service/qna";

    }


    /**
     * 답변 관련
     */

    @PostMapping("/answers/write")
    public String writeAnswer(@Valid AnswerRequestDto dto,
                              BindingResult r,
                              RedirectAttributes ra,
                              Authentication authentication) {

        if (r.hasErrors()) {

            log.info("[QnaAdminController writeAnswer] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/qna/" + dto.getQid();

        }

        User user = (User) authentication.getPrincipal();
        String nickname = user.getNickname();
        dto.setUsername(nickname); // 현재 사용자 정보 내 닉네임을 받아서 dto에 주입

        qnaAdminService.saveAnswer(dto);

        return "redirect:/admin/service/qna/" + dto.getQid();

    }

    @PostMapping("/answers/{id}/delete")
    public String deleteAnswer(@PathVariable("id") Long id, Long qid) {

        qnaAdminService.deleteAnswer(id);

        return "redirect:/admin/service/qna/" + qid;

    }


    /**
     * 카테고리 관련
     */

    @GetMapping("/categories")
    public String getQuestionCategoryList(ModelMap map) {

        List<QuestionCategoryDto> dtos = qnaAdminService.getQuestionCategoryList();
        map.addAttribute("dtos", dtos);

        return "admin/service/qna/qna_category_list";

    }

    @GetMapping("/categories/{id}")
    public String getQuestionCategory(@PathVariable("id") Long id, ModelMap map) {

        QuestionCategoryDto dto = qnaAdminService.getQuestionCategory(id);
        map.addAttribute("dto", dto);

        return "admin/service/qna/qna_category_detail";

    }

    @GetMapping("/categories/write")
    public String getCategoryWriteForm() {

        return "admin/service/qna/qna_category_write";

    }

    @PostMapping("/categories/write")
    public String writeQuestionCategory(@Valid QuestionCategoryRequestDto dto,
                                        BindingResult r,
                                        RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[QnaAdminController writeQuestionCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/qna/categories/write";

        }

        qnaAdminService.saveQuestionCategory(dto);

        return "redirect:/admin/service/qna/categories";

    }

    @GetMapping("/categories/{id}/modify")
    public String getCategoryModifyForm(@PathVariable("id") Long id, ModelMap map) {

        QuestionCategoryModifyRequestDto dto = QuestionCategoryModifyRequestDto.from(qnaAdminService.getQuestionCategory(id));
        map.addAttribute("dto", dto);

        return "admin/service/qna/qna_category_modify";

    }

    @PostMapping("/categories/{id}/modify")
    public String modifyQuestionCategory(@PathVariable("id") Long id,
                                         @Valid QuestionCategoryModifyRequestDto dto,
                                         BindingResult r,
                                         RedirectAttributes ra) {

        if (r.hasErrors()) {

            log.info("[QnaAdminController modifyQuestionCategory] validation error");
            ra.addFlashAttribute("errors", r.getAllErrors());

            return "redirect:/admin/service/qna/" + id + "/modify";

        }

        qnaAdminService.updateQuestionCategory(dto);

        return "redirect:/admin/service/qna/categories/" + id;
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteQuestionCategory(@PathVariable("id") Long id) {

        qnaAdminService.deleteQuestionCategory(id);

        return "redirect:/admin/service/qna/categories";

    }


}
