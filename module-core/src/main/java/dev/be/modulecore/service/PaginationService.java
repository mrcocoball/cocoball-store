package dev.be.modulecore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j(topic = "SERVICE")
@Service
@RequiredArgsConstructor
public class PaginationService {

    private static final int BAR_LENGTH = 10;

    public PageImpl listToPage(List<?> data, Pageable pageable) {

        if (ObjectUtils.isEmpty(data) || data.isEmpty() || data == null) {
            log.info("[PaginationService listToPage] result is null");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), data.size());
        return new PageImpl<>(data.subList(start, end), pageable, data.size());
    }

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        // 기본 골자는 현재 페이지 - (길이 / 2) 겠지만 0보다 작으면 0 반환
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);
        // startNumber + 길이가 totalPages를 넘지 않게

        return IntStream.range(startNumber, endNumber).boxed().collect(Collectors.toList());
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
