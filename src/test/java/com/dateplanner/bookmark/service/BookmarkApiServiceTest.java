package com.dateplanner.bookmark.service;

import com.dateplanner.bookmark.dto.BookmarkDto;
import com.dateplanner.bookmark.repository.BookmarkRepository;
import com.dateplanner.fixture.Fixture;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.dto.PlaceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 북마크 화면 처리 서비스 - 북마크 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookmarkApiServiceTest {

    @Autowired
    private BookmarkApiService sut;

    @MockBean
    private BookmarkRepository bookmarkRepository;


    @DisplayName("READ - 북마크 리스트 조회")
    @Test
    public void 북마크_리스트_조회_성공() {

        // Given
        String uid = "test";
        Pageable pageable = Pageable.ofSize(10);
        given(bookmarkRepository.findByUser_Uid(uid)).willReturn(Collections.emptyList());

        // When
        Page<BookmarkDto> result = sut.getBookmarkList(uid, pageable);

        // Then
        assertThat(result).isEmpty();
        then(bookmarkRepository).should().findByUser_Uid(uid);

    }

}