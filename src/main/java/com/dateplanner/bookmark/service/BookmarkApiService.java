package com.dateplanner.bookmark.service;

import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.bookmark.dto.BookmarkDto;
import com.dateplanner.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkApiService {

    private final BookmarkRepository bookmarkRepository;
    private final PaginationService paginationService;

    public Page<BookmarkDto> getBookmarkList(String email, Pageable pageable) {

        // 인덱스 계산용 시간 측정
        long beforeTime = System.currentTimeMillis();

        log.info("[BookmarkApiService getBookmarkList] get bookmark list start...");
        List<BookmarkDto> dtos = bookmarkRepository.findByUser_Email(email).stream().map(BookmarkDto::from).collect(Collectors.toList());
        log.info("[BookmarkApiService getBookmarkList] get bookmark list complete, size : {}", dtos.size());

        // 인덱스 계산용 시간 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime-beforeTime));

        return paginationService.listToPage(dtos, pageable);
    }

}
