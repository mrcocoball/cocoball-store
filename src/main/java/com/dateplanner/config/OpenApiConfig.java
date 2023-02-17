package com.dateplanner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                .info(new Info().title("Date-planner API 서버")
                        .description("API 명세서와 테스트를 위한 API를 제공하고 있습니다")
                        .version("v1")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("더블코코볼 mrcocoball")
                                .email("gunmaru93@gmai.com")
                                .url("https://github.com/mrcocoball/date-planner")));
    }


    /*

    // 그룹 설정된 API들만 문서에 노출시킬 수 있게 한다
    @Bean
    public GroupedOpenApi sampleGroupOpenApi() {
        String[] paths = {"/diary/**"};

        return GroupedOpenApi.builder().group("샘플 API").pathsToMatch(paths).build();
    }

     */
}
