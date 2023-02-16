package com.dateplanner.admin.place.service;

import com.dateplanner.admin.place.dto.PlaceCrawlingDto;
import com.dateplanner.api.PaginationService;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceCrawlingService {

    private final PlaceRepository placeRepository;
    private final PaginationService paginationService;
    private static final String BASE_URL = "https://place.map.kakao.com/";
    private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

    @Value("${com.dateplanner.webdriver.path}")
    private String webDriverPath;


    public Page<PlaceCrawlingDto> searchAndCrawling(Pageable pageable) {

        List<String> placeIds = placeRepository.findPlaceIdByImageUrlIsNull();

        List<PlaceCrawlingDto> results = new ArrayList<>();

        if (ObjectUtils.isEmpty(placeIds) || placeIds == null) {
            return paginationService.listToPage(results, pageable);
        }

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        int count = 0;

        for (String placeId : placeIds) {
            PlaceCrawlingDto dto = crawling(placeId);
            results.add(dto);
            count += 1;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, placeIds.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return paginationService.listToPage(results, pageable);
    }

    public List<PlaceCrawlingDto> searchAndCrawling() {

        List<String> placeIds = placeRepository.findPlaceIdByImageUrlIsNull();

        List<PlaceCrawlingDto> results = new ArrayList<>();

        if (ObjectUtils.isEmpty(placeIds) || placeIds == null) {
            return Collections.emptyList();
        }

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        int count = 0;

        for (String placeId : placeIds) {
            PlaceCrawlingDto dto = crawling(placeId);
            results.add(dto);
            count += 1;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, placeIds.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return results;
    }


    private PlaceCrawlingDto crawling(String placeId) {

        PlaceCrawlingDto dto = new PlaceCrawlingDto();
        dto.setPlaceId(placeId);

        System.setProperty(WEB_DRIVER_ID, webDriverPath);

        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments("--start-maximized")
                .addArguments("--disable-popup-blocking")
                .addArguments("headless");

        WebDriver webDriver = new ChromeDriver(chromeOptions);

        try {
            webDriver.get(BASE_URL + placeId + "?service=search_pc");
            Thread.sleep(500);

            // 설명 저장
            log.info("[PlaceCrawlingService crawling] description crawling}");
            if (webDriver.findElements(By.cssSelector("span.tag_g > a")).size() != 0) {
                List<WebElement> descriptions = webDriver.findElements(By.cssSelector("span.tag_g > a"));
                if (!ObjectUtils.isEmpty(descriptions) || descriptions != null) {
                    for (WebElement description : descriptions) {
                        log.info("[PlaceCrawlingService crawling] description : {}", description.getText());
                        dto.addTag(description.getText());
                    }
                }
            }

            // 이미지 저장
            log.info("[PlaceCrawlingService crawling] image crawling}");
            if (webDriver.findElement(By.className("size_l")) != null) {
                WebElement img_info = webDriver.findElement(By.className("size_l"));
                // 현 상태에서 getCssValue()로 background-image를 가져오려 하였으나 none 반환 (이미지 지연 로딩)

                if (img_info != null) {
                    img_info.click();
                    Thread.sleep(500);

                    if (webDriver.findElement(By.className("img_photo")) != null) {
                        String imageUrl = webDriver.findElement(By.className("img_photo")).getAttribute("src");
                        log.info("[PlaceCrawlingService crawling] imageUrl : {}", imageUrl);
                        dto.setImageUrl(imageUrl);
                    }
                }
            }
        } catch (InterruptedException e) {
            return dto;
        } catch (NoSuchElementException e) {
            return dto;
        } finally {
            webDriver.close();
        }

        return dto;

    }

}
