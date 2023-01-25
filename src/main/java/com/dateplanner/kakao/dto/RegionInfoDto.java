package com.dateplanner.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegionInfoDto {

    @JsonProperty("selected_region")
    private String selectedRegion;
}
