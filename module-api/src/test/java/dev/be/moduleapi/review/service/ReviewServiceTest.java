package dev.be.moduleapi.review.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.review.dto.ReviewRequestDto;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.review.Review;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.place.PlaceRepository;
import dev.be.modulecore.repositories.review.ReviewRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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