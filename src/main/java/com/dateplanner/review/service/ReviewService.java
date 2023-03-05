package com.dateplanner.review.service;

import com.dateplanner.advice.exception.PlaceNotFoundApiException;
import com.dateplanner.advice.exception.ReviewNotFoundApiException;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.review.dto.ReviewRequestDto;
import com.dateplanner.review.entity.Review;
import com.dateplanner.review.repository.ReviewRepository;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;


    public Long saveReview(ReviewRequestDto dto) {

        /**
         * PlaceId를 현재 보고 있는 장소 Dto의 placeId로 주입이 가능할 경우
         * 이렇게 처리하고, 그게 아니라면 수정이 필요할 것으로 보임
         */

        User user = userRepository.findByNickname(dto.getNickname()).orElseThrow(UserNotFoundApiException::new);
        Place place = placeRepository.findByPlaceId(dto.getPlaceId()).orElseThrow(PlaceNotFoundApiException::new);

        Review review = reviewRepository.save(dto.toEntity(user, place, dto.getTitle(), dto.getDescription(), dto.getReviewScore()));
        place.addScoreAndCount(dto.getReviewScore()); // 리뷰 점수 반영

        return review.getId();
    }

    public Long updateReview(ReviewRequestDto dto) {

        Review review = reviewRepository.findById(dto.getId()).orElseThrow(ReviewNotFoundApiException::new);
        Place place = review.getPlace();
        place.subtractScoreAndCount(review.getReviewScore()); // 기존 리뷰에 계산된 리뷰 점수 차감

        review.changeTitle(dto.getTitle());
        review.changeDescription(dto.getDescription());
        review.changeReviewScore(dto.getReviewScore());

        place.addScoreAndCount(dto.getReviewScore()); // 업데이트된 리뷰 점수 반영

        // 첨부파일 처리
        review.clearImages();

        if(dto.getFileNames() != null) {
            for (String fileName : dto.getFileNames()) {
                String[] arr = fileName.split("_", 2);
                review.addImage(arr[0], arr[1]);
            }
        }

        reviewRepository.save(review);

        return review.getId();
    }

    public void deleteReview(Long id) {

        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundApiException::new);
        Place place = review.getPlace();
        place.subtractScoreAndCount(review.getReviewScore()); // 기존 리뷰에 계산된 리뷰 점수 차감

        reviewRepository.deleteById(id);
    }

}
