package com.dateplanner.review.service;

import com.dateplanner.advice.exception.ReviewNotFoundApiException;
import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.review.dto.ReviewDto;
import com.dateplanner.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewApiService {

    private final ReviewRepository reviewRepository;
    private final PaginationService paginationService;


    public Page<ReviewDto> getReviewListByPlaceId(String placeId, Pageable pageable) {

        // 인덱스 계산용 시간 측정
        long beforeTime = System.currentTimeMillis();

        log.info("[ReviewApiService getReviewList] get review list start...");
        List<ReviewDto> dtos = reviewRepository.findByKpid(placeId).stream().map(ReviewDto::from).collect(Collectors.toList());
        log.info("[ReviewApiService getReviewList] get review list complete, size : {}", dtos.size());

        // 인덱스 계산용 시간 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime-beforeTime));

        return paginationService.listToPage(dtos, pageable);
    }

    public Page<ReviewDto> getReviewListByNickname(String nickname, Pageable pageable) {

        log.info("[ReviewApiService getReviewList] get review list start...");
        List<ReviewDto> dtos = reviewRepository.findByUser_Nickname(nickname).stream().map(ReviewDto::from).collect(Collectors.toList());
        log.info("[ReviewApiService getReviewList] get review list complete, size : {}", dtos.size());

        return paginationService.listToPage(dtos, pageable);
    }


    public ReviewDto getReview(Long id) {

        return reviewRepository.findById(id).map(ReviewDto::from).orElseThrow(ReviewNotFoundApiException::new);
    }

}
