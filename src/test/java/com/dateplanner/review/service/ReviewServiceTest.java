package com.dateplanner.review.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.review.dto.ReviewRequestDto;
import com.dateplanner.review.entity.Review;
import com.dateplanner.review.repository.ReviewRepository;
import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(user));
        given(placeRepository.findByPlaceId(dto.getPlaceId())).willReturn(Optional.of(place));
        given(reviewRepository.save(any(Review.class))).willReturn(Fixture.review());

        // When
        sut.saveReview(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getNickname());
        then(placeRepository).should().findByPlaceId(dto.getPlaceId());
        then(reviewRepository).should().save(any(Review.class));

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