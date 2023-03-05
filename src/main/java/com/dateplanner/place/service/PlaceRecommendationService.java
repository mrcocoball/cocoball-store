package com.dateplanner.place.service;

import com.dateplanner.advice.exception.SearchResultNotFoundException;
import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.place.dto.PlaceRecommendationDto;
import com.dateplanner.place.repository.PlaceRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceRecommendationService {

    private final PlaceRecommendationRepository placeRecommendationRepository;
    private final PaginationService paginationService;


    public Page<PlaceRecommendationDto> getPlaceRecommendation(String region1, String region2, String region3, Pageable pageable) {

        List<PlaceRecommendationDto> result = placeRecommendationRepository.placeRecommendation(region1, region2, region3);

        if (Objects.isNull(result) || result.isEmpty()) {
            log.error("[PlaceRecommendationService getPlaceRecommendation] recommendation result is null");
            throw new SearchResultNotFoundException();
        }

        for (PlaceRecommendationDto dto : result) {
            double avgScore = PlaceRecommendationDto.calculateAvgScore(dto.getReviewScore(), dto.getReviewCount());
            dto.setAvgReviewScore(avgScore);
        }

        return paginationService.listToPage(result, pageable);

    }

}
