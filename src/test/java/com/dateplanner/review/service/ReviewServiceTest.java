package com.dateplanner.review.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.review.dto.ReviewRequestDto;
import com.dateplanner.review.entity.Review;
import com.dateplanner.review.repository.ReviewRepository;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 리뷰 서비스 - 리뷰 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService sut;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceRepository placeRepository;


    @DisplayName("CREATE - 리뷰 저장")
    @Test
    public void 리뷰_저장_성공() {

        // Given
        User user = Fixture.user();
        Place place = Fixture.place();
        ReviewRequestDto dto = Fixture.reviewRequestDto();
        given(userRepository.findByUid(dto.getUid())).willReturn(Optional.of(user));
        given(placeRepository.findByPlaceId(dto.getPlaceId())).willReturn(Optional.of(place));
        given(reviewRepository.save(any(Review.class))).willReturn(Fixture.review());

        // When
        sut.saveReview(dto);

        // Then
        then(userRepository).should().findByUid(dto.getUid());
        then(placeRepository).should().findByPlaceId(dto.getPlaceId());
        then(reviewRepository).should().save(any(Review.class));

    }

    @Disabled("단일 테스트로 돌리기 어려울 것 같다...")
    @DisplayName("UPDATE - 리뷰 수정")
    @Test
    public void 리뷰_수정_성공() {

        // Given
        User user = Fixture.user();
        Place place = Fixture.place();
        Review review = Fixture.review();
        ReviewRequestDto oldDto = Fixture.reviewRequestDto();
        ReviewRequestDto dto = Fixture.reviewUpdateRequestDto();
        given(userRepository.getReferenceById(dto.getUid())).willReturn(user);
        given(placeRepository.getReferenceById(dto.getPid())).willReturn(place);
        given(reviewRepository.getReferenceById(dto.getId())).willReturn(review);

        // When
        userRepository.save(user);
        placeRepository.save(place);
        reviewRepository.save(review);
        Long updatedReviewId = sut.updateReview(dto);

        // Then
        assertThat(review)
                .hasFieldOrPropertyWithValue("title", dto.getTitle())
                .hasFieldOrPropertyWithValue("description", dto.getDescription())
                .hasFieldOrPropertyWithValue("reviewScore", dto.getReviewScore());
        then(userRepository).should().getReferenceById(dto.getUid());
        then(placeRepository).should().findByPlaceId(dto.getPlaceId());
        then(reviewRepository).should().getReferenceById(dto.getId());

    }

    @DisplayName("DELETE - 리뷰 삭제")
    @Test
    public void 리뷰_삭제_성공() {

        // Given
        Long id = 1L;
        given(reviewRepository.findById(id)).willReturn(Optional.of(Fixture.review()));
        willDoNothing().given(reviewRepository).deleteById(id);

        // When
        sut.deleteReview(id);

        // Then
        then(reviewRepository).should().findById(id);
        then(reviewRepository).should().deleteById(id);

    }

}