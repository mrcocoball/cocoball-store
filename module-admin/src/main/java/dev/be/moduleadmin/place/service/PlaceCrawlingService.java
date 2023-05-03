package dev.be.moduleadmin.place.service;

import dev.be.moduleadmin.place.dto.PlaceCrawlingDto;
import dev.be.modulecore.repositories.support.PlaceAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceCrawlingService {

    private final PlaceAdminRepository placeAdminRepository;
    private static final String BASE_URL = "https://place.map.kakao.com/";
    private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

    @Value("${com.dateplanner.webdriver.path}")
    private String webDriverPath;


    public List<PlaceCrawlingDto> searchAndCrawlingV1() {

        List<String> placeIds = placeAdminRepository.findPlaceIdByImageUrlIsNull();

        List<PlaceCrawlingDto> results = new ArrayList<>();

        if (ObjectUtils.isEmpty(placeIds) || placeIds == null) {
            return Collections.emptyList();
        }

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        int count = 0;

        for (String placeId : placeIds) {
            PlaceCrawlingDto dto = crawlingV1(placeId);
            results.add(dto);
            count += 1;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, placeIds.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return results;
    }


    private PlaceCrawlingDto crawlingV1(String placeId) {

        PlaceCrawlingDto dto = new PlaceCrawlingDto();
        dto.setPlaceId(placeId);

        System.setProperty(WEB_DRIVER_ID, webDriverPath);

        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments("--start-maximized")
                .addArguments("--disable-popup-blocking")
                .addArguments("--remote-allow-origins=*") // 웹소켓 연결 실패 방지용
                .addArguments("--single-process") // docker 환경용 추가
                .addArguments("--disable-dev-shm-usage") // docker 환경용 추가
                .addArguments("--no-sandbox") // docker 환경용 추가
                .addArguments("--headless"); // 문법 수정

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

            // 평점 저장
            log.info("[PlaceCrawlingService crawling] review crawling}");
            if (webDriver.findElement(By.className("color_b")) != null) {
                String reviewScoreOrigin = webDriver.findElement(By.className("color_b")).getText();
                dto.setReviewScore(Double.valueOf(reviewScoreOrigin).doubleValue());
                log.info("[PlaceCrawlingService crawling] reviewScore : {}", dto.getReviewScore());
            }
            if (webDriver.findElement(By.cssSelector("div.location_evaluation > a.link_evaluation > span.color_g")) != null) {
                String reviewCountOrigin = webDriver.findElement(By.cssSelector("div.location_evaluation > a.link_evaluation > span.color_g")).getText();
                dto.setReviewCount(Long.valueOf(reviewCountOrigin.substring(1, reviewCountOrigin.length()-1)).longValue());
                log.info("[PlaceCrawlingService crawling] reviewCount : {}", dto.getReviewCount());
            }

            if (dto.getReviewScore() != 0d && dto.getReviewCount() != 0) {
                double totalReviewScore = dto.getReviewScore() * dto.getReviewCount();
                dto.setReviewScore(Math.round(totalReviewScore));
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

    /**
     * 장소 크롤링 후 업데이트
     */

    public Long updatePlacesV1(List<PlaceCrawlingDto> dtos) {

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        Long count = 0L;

        for (PlaceCrawlingDto dto : dtos) {
            List<String> tags = dto.getTags();
            StringJoiner stringJoiner = new StringJoiner(", ");
            stringJoiner.setEmptyValue("");
            if (!ObjectUtils.isEmpty(tags) || tags != null) {
                for (String tag : tags) {
                    stringJoiner.add(tag);
                }
            }
            String description = String.valueOf(stringJoiner);
            if (description.equals("")) {description = null;}
            placeAdminRepository.updateImageUrlAndDescriptionAndReviews(dto.getPlaceId(), dto.getImageUrl(), description, (long) dto.getReviewScore(), dto.getReviewCount());
            count += 1L;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, dtos.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return count;
    }

}
