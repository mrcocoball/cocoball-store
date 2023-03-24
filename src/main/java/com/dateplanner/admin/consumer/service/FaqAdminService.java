package com.dateplanner.admin.consumer.service;

import com.dateplanner.admin.consumer.dto.*;
import com.dateplanner.admin.consumer.entity.FavoriteAnswer;
import com.dateplanner.admin.consumer.entity.FavoriteQuestionCategory;
import com.dateplanner.admin.consumer.repository.FavoriteAnswerAdminRepository;
import com.dateplanner.admin.consumer.repository.FavoriteQuestionCategoryAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@Service
@RequiredArgsConstructor
@Transactional
public class FaqAdminService {

    private final FavoriteQuestionCategoryAdminRepository favoriteQuestionCategoryAdminRepository;
    private final FavoriteAnswerAdminRepository favoriteAnswerAdminRepository;


    /**
     * 카테고리 관련
     */

    @Transactional(readOnly = true)
    public List<FavoriteQuestionCategoryDto> getFavoriteQuestionCategoryList() {

        return favoriteQuestionCategoryAdminRepository.findAll().stream().map(FavoriteQuestionCategoryDto::from).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public FavoriteQuestionCategoryDto getFavoriteQuestionCategory(Long id) {

        return favoriteQuestionCategoryAdminRepository.findById(id).map(FavoriteQuestionCategoryDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveFavoriteQuestionCategory(FavoriteQuestionCategoryRequestDto dto) {

        FavoriteQuestionCategory favoriteQuestionCategory = favoriteQuestionCategoryAdminRepository.save(dto.toEntity(dto.getCategoryName()));

        return favoriteQuestionCategory.getId();

    }

    public Long updateFavoriteQuestionCategory(FavoriteQuestionCategoryModifyRequestDto dto) {

        FavoriteQuestionCategory favoriteQuestionCategory = favoriteQuestionCategoryAdminRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        favoriteQuestionCategory.changeCategoryName(dto.getCategoryName());

        return favoriteQuestionCategory.getId();

    }

    public void deleteFavoriteQuestionCategory(Long id) {

        favoriteQuestionCategoryAdminRepository.deleteById(id);

    }


    /**
     * 카테고리 별 FAQ 관련
     */

    @Transactional(readOnly = true)
    public FavoriteAnswerDto getFavoriteAnswer(Long id) {

        return favoriteAnswerAdminRepository.findById(id).map(FavoriteAnswerDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public Long saveFavoriteAnswer(FavoriteAnswerRequestDto dto) {

        FavoriteQuestionCategory favoriteQuestionCategory = favoriteQuestionCategoryAdminRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        FavoriteAnswer favoriteAnswer = favoriteAnswerAdminRepository.save(dto.toEntity(dto.getTitle(), dto.getDescription(), favoriteQuestionCategory));
        favoriteQuestionCategory.addFavoriteAnswer(favoriteAnswer);

        return favoriteQuestionCategory.getId();

    }

    public Long updateFavoriteAnswer(FavoriteAnswerModifyRequestDto dto) {

        FavoriteAnswer favoriteAnswer = favoriteAnswerAdminRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        favoriteAnswer.changeTitle(dto.getTitle());
        favoriteAnswer.changeDescription(dto.getDescription());

        return favoriteAnswer.getId();

    }

    public void deleteFavoriteAnswer(Long id) {

        favoriteAnswerAdminRepository.deleteById(id);

    }

}
