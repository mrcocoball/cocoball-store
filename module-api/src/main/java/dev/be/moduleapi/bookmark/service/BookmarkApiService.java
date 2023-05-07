package dev.be.moduleapi.bookmark.service;

import dev.be.moduleapi.bookmark.dto.BookmarkDto;
import dev.be.modulecore.repositories.bookmark.BookmarkRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@Timed("business.service.bookmark")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkApiService {

    private final BookmarkRepository bookmarkRepository;

    public Page<BookmarkDto> getBookmarkList(String email, Pageable pageable) {

        Page<BookmarkDto> dtos = bookmarkRepository.findByUser_Email(email, pageable).map(bookmark -> BookmarkDto.from(bookmark));

        return dtos;
    }

}
