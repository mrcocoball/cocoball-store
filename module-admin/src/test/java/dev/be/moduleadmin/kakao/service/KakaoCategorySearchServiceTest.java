package dev.be.moduleadmin.kakao.service;

import dev.be.moduleadmin.kakao.dto.DocumentDto;
import dev.be.moduleadmin.kakao.dto.KakaoApiResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 카카오 API - 카카오 카테고리 장소 검색 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class KakaoCategorySearchServiceTest {

    @Autowired
    private KakaoCategorySearchService sut;


    @DisplayName("입력 위치 정보가 유효할 경우 document 반환")
    @Test
    public void 카테고리_장소_검색_성공() {

        // Given
        String category = "CE7";
        double y = 37.514322572335935;
        double x = 127.06283102249932;
        int radius = 5;
        List<String> categories = new ArrayList<>();
        categories.add(category);

        // When
        KakaoApiResponseDto result = sut.requestCategorySearch(y, x, radius, categories);
        List<DocumentDto> resultList = result.getDocumentList();

        // Then
        assertThat(resultList).isNotEmpty();
    }

}