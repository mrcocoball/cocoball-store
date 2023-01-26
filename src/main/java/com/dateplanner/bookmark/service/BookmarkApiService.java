package com.dateplanner.bookmark.service;

import com.dateplanner.api.PaginationService;
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

    public Page<BookmarkDto> getBookmarkList(String uid, Pageable pageable) {

        log.info("[BookmarkApiService getBookmarkList] get bookmark list start...");
        List<BookmarkDto> dtos = bookmarkRepository.findByUser_Uid(uid).stream().map(BookmarkDto::from).collect(Collectors.toList());
        log.info("[BookmarkApiService getBookmarkList] get bookmark list complete, size : {}", dtos.size());

        return paginationService.listToPage(dtos, pageable);
    }

}
