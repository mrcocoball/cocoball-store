package com.dateplanner.review.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.review.dto.ReviewDto;
import com.dateplanner.review.entity.Review;
import com.dateplanner.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 리뷰 화면 처리 서비스 - 리뷰 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReviewApiServiceTest {

    @Autowired
    private ReviewApiService sut;

    @MockBean
    private ReviewRepository reviewRepository;

    @DisplayName("READ - 장소 내 리뷰 리스트 조회")
    @Test
    public void 장소_내_리뷰_리스트_조회_성공() {

        // Given
        String placeId = "1";
        Pageable pageable = Pageable.ofSize(10);
        given(reviewRepository.findByKpid(placeId)).willReturn(Collections.emptyList());

        // When
        Page<ReviewDto> result = sut.getReviewListByPlaceId(placeId, pageable);

        // Then
        assertThat(result).isEmpty();
        then(reviewRepository).should().findByKpid(placeId);

    }

    @DisplayName("READ - 사용자 작성 리뷰 리스트 조회")
    @Test
    public void 사용자_작성_리뷰_리스트_조회_성공() {

        // Given
        String nickname = "test";
        Pageable pageable = Pageable.ofSize(10);
        given(reviewRepository.findByUser_Nickname(nickname)).willReturn(Collections.emptyList());

        // When
        Page<ReviewDto> result = sut.getReviewListByNickname(nickname, pageable);

        // Then
        assertThat(result).isEmpty();
        then(reviewRepository).should().findByUser_Nickname(nickname);

    }

    @DisplayName("READ - 리뷰 단건 조회")
    @Test
    public void 리뷰_단건_조회_성공() {

        // Given
        Long id = 1L;
        Review review = Fixture.review();
        given(reviewRepository.findById(id)).willReturn(Optional.of(review));

        // When
        ReviewDto dto = sut.getReview(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", review.getId())
                .hasFieldOrPropertyWithValue("placeId", review.getKpid());
        then(reviewRepository).should().findById(id);

    }


}