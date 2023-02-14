package com.dateplanner.place.controller;

import com.dateplanner.api.service.ResponseService;
import com.dateplanner.config.TestSecurityConfig;
import com.dateplanner.fixture.Fixture;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.service.PlaceApiService;
import com.dateplanner.place.service.PlaceService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@DisplayName("[통합] 장소 검색 API 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(PlaceApiController.class)
class PlaceApiControllerTest {

    private final MockMvc mvc;

    @MockBean
    private PlaceService placeService;
    @MockBean
    private PlaceApiService placeApiService;
    @MockBean
    private ResponseService responseService;

    public PlaceApiControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @WithAnonymousUser
    // @WithUserDetails(value = "testuser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET] 장소 검색 /api/v1/places - 성공")
    @Test
    public void 장소_검색_성공() throws Exception {

        // Given
        KakaoApiResponseDto testKakaoApiResponseDto = new KakaoApiResponseDto();
        DocumentDto testDocumentDto = Fixture.documentDto();
        List<DocumentDto> testDocumentList = new ArrayList<>();
        testDocumentList.add(testDocumentDto);
        testKakaoApiResponseDto.setDocumentList(testDocumentList);
        String address = "서울 관악구";
        String testRegion1 = testDocumentDto.getRegion1DepthName();

        List<String> testRegion2List = new ArrayList<>();
        String testRegion2 = "관악구";
        testRegion2List.add(testRegion2);

        List<String> testCategoryList = new ArrayList<>();
        String testCategory = "CE7";
        testCategoryList.add(testCategory);

        Pageable pageable = Pageable.ofSize(10);

        given(placeApiService.getPlaces(testDocumentDto, testKakaoApiResponseDto, testRegion2List, testCategoryList, pageable)).willReturn(Page.empty());
        //given(responseService.getPageResult(Page.empty())).willReturn(Page.empty());

        // When & Then
        mvc.perform(
                        get("/api/v1/places")
                                .queryParam("address", address)
                                .queryParam("categories", testCategory)
                )
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.size()").value("0"))
                .andDo(MockMvcResultHandlers.print());
        then(placeApiService.getPlaces(testDocumentDto, testKakaoApiResponseDto, testRegion2List, testCategoryList, pageable));
        //then(responseService.getPageResult(Page.empty()));

    }


    @DisplayName("[GET] 단일 장소 조회 /api/v1/places/{place_id} - 성공")
    @Test
    public void 단일장소_조회_성공() {

        // Given

        // When

        // Then

    }


}