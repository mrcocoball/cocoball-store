package dev.be.moduleapi.review.service;

import dev.be.moduleapi.advice.exception.ReviewNotFoundApiException;
import dev.be.moduleapi.review.dto.ReviewDto;
import dev.be.modulecore.repositories.review.ReviewRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@Timed("business.service.review")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewApiService {

    private final ReviewRepository reviewRepository;


    public Page<ReviewDto> getReviewListByPlaceId(String placeId, Pageable pageable) {

        Page<ReviewDto> dtos = reviewRepository.findByKpid(placeId, pageable).map(review -> ReviewDto.from(review));

        return dtos;
    }

    public Page<ReviewDto> getReviewListByNickname(String nickname, Pageable pageable) {

        return reviewRepository.findByUser_Nickname(nickname, pageable).map(review -> ReviewDto.from(review));
    }


    public ReviewDto getReview(Long id) {

        return reviewRepository.findById(id).map(ReviewDto::from).orElseThrow(ReviewNotFoundApiException::new);
    }

}
