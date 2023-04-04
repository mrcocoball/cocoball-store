package dev.be.moduleapi.bookmark.service;

import dev.be.moduleapi.bookmark.dto.BookmarkDto;
import dev.be.modulecore.repositories.bookmark.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkApiService {

    private final BookmarkRepository bookmarkRepository;

    public Page<BookmarkDto> getBookmarkList(String email, Pageable pageable) {

        // 인덱스 계산용 시간 측정
        long beforeTime = System.currentTimeMillis();

        log.info("[BookmarkApiService getBookmarkList] get bookmark list start...");
        Page<BookmarkDto> dtos = bookmarkRepository.findByUser_Email(email, pageable).map(bookmark -> BookmarkDto.from(bookmark));
        log.info("[BookmarkApiService getBookmarkList] get bookmark list complete, size : {}", dtos.getSize());

        // 인덱스 계산용 시간 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime-beforeTime));

        return dtos;
    }

}
