package dev.be.moduleapi.bookmark.service;

import dev.be.moduleapi.advice.exception.BookmarkDuplicateException;
import dev.be.moduleapi.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleapi.advice.exception.UserNotFoundApiException;
import dev.be.moduleapi.bookmark.dto.BookmarkDto;
import dev.be.modulecore.domain.bookmark.Bookmark;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.bookmark.BookmarkRepository;
import dev.be.modulecore.repositories.place.PlaceRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public BookmarkDto saveBookmark(String nickname, String placeId) {

        // 중복 체크
        log.info("[BookmarkService saveBookmark] bookmark duplication checking");

        if (isExist(placeId, nickname)) { throw new BookmarkDuplicateException(); }

        log.info("[BookmarkService saveBookmark] bookmark duplication check complete");

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundApiException::new);
        Place place = placeRepository.findByPlaceId(placeId).orElseThrow(PlaceNotFoundApiException::new);

        Bookmark bookmark = Bookmark.of(user, place, placeId);
        bookmarkRepository.save(bookmark);

        return BookmarkDto.from(bookmark);
    }

    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    public void deleteBookmarkByPlaceId(String placeId) {
        bookmarkRepository.deleteByPlace_PlaceId(placeId);
    }

    public boolean isExist(String placeId, String nickname) {
        log.info("[BookmarkService isExist] checking...");
        return bookmarkRepository.isBookmarked(placeId, nickname).size() >= 1;
    }

}
