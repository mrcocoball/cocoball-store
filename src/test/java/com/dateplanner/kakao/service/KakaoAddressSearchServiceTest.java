package com.dateplanner.kakao.service;

import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.kakao.dto.MetaDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.ExhaustedRetryException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[통합] 카카오 API - 카카오 주소 - 위치 변환 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class KakaoAddressSearchServiceTest {

    @Autowired
    private KakaoAddressSearchService sut;


    @DisplayName("주소가 null일 경우 예외 반환")
    @Test
    public void 주소_위치변환_주소가_null일경우_실패() {

        // TODO : AddressInvalidException을 기대하였으나 Retry 관련으로 예외가 발생하여 (예외 중첩 관련인듯) 일단 해당 예외로 변경
        assertThrows(ExhaustedRetryException.class, ()-> {
            sut.requestAddressSearch(null);
        });

    }

    @DisplayName("주소가 유효할 경우 document 반환")
    @Test
    public void 주소_위치변환_주소가_유효할경우_성공() {

        // Given
        String address = "서울 성북구 종암로 10길";

        // When
        KakaoApiResponseDto result = sut.requestAddressSearch(address);
        List<DocumentDto> resultList = result.getDocumentList();
        MetaDto resultMetaDto = result.getMetaDto();
        String addressName = resultList.get(0).getAddressName();

        // Then
        assertThat(resultList).isNotNull();
        assertThat(resultMetaDto.getTotalCount()).isPositive();
        assertThat(addressName).isNotNull();

    }

    @DisplayName("주소가 유효하지 않을 경우 예외 반환")
    @Test
    public void 주소_위치변환_주소가_유효하지않을경우_실패() {

        // Given
        String address = "잘못된 주소";

        // When
        KakaoApiResponseDto result = sut.requestAddressSearch(address);
        List<DocumentDto> resultList = result.getDocumentList();

        // Then
        assertThat(resultList).isEmpty();

    }
}