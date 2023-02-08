package com.dateplanner.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Slf4j(topic = "SERVICE")
@Service
@RequiredArgsConstructor
public class PaginationService {

    public PageImpl listToPage(List<?> data, Pageable pageable) {

        if (ObjectUtils.isEmpty(data) || data.isEmpty() || data == null) {
            log.info("[PaginationService listToPage] result is null");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), data.size());
        return new PageImpl<>(data.subList(start, end), pageable, data.size());
    }

}
