package com.dateplanner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// 명세 전체에 대한 공통 설명을 한다
@OpenAPIDefinition(
        info = @Info(
                title = "Date-planner API 명세서",
                description = "API 명세서",
                version = "v1",
                contact = @Contact(
                        name = "더블코코볼",
                        email = "gunmaru93@gmail.com"
                )
        )
)

@Configuration
public class OpenApiConfig {

    /*

    // 그룹 설정된 API들만 문서에 노출시킬 수 있게 한다
    @Bean
    public GroupedOpenApi sampleGroupOpenApi() {
        String[] paths = {"/diary/**"};

        return GroupedOpenApi.builder().group("샘플 API").pathsToMatch(paths).build();
    }

     */
}
