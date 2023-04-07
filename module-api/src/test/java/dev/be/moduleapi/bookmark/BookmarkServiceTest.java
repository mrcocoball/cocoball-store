package dev.be.moduleapi.bookmark;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleapi.bookmark.service.BookmarkService;
import dev.be.modulecore.domain.bookmark.Bookmark;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.bookmark.BookmarkRepository;
import dev.be.modulecore.repositories.place.PlaceRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 북마크 서비스 - 북마크 저장 테스트")
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks private BookmarkService sut;

    @Mock private BookmarkRepository bookmarkRepository;

    @Mock private PlaceRepository placeRepository;

    @Mock private UserRepository userRepository;


    @DisplayName("CREATE - 북마크 저장")
    @Test
    public void 북마크_저장_성공() {

        // Given
        String nickname = "test";
        String kpid = "test";
        User user = Fixture.user();
        Place place = Fixture.place();
        given(userRepository.findByNickname(nickname)).willReturn(Optional.of(user));
        given(placeRepository.findByPlaceId(kpid)).willReturn(Optional.of(place));
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(Fixture.bookmark());

        // When
        sut.saveBookmark(nickname, kpid);

        // Then
        then(userRepository).should().findByNickname(nickname);
        then(placeRepository).should().findByPlaceId(kpid);
        then(bookmarkRepository).should().save(any(Bookmark.class));

    }

    @DisplayName("CREATE - 북마크 저장 - 실패(존재하지 않는 장소)")
    @Test
    public void 북마크_저장_실패() {

        // Given
        String nickname = "test";
        String kpid = "test";
        User user = Fixture.user();
        given(userRepository.findByNickname(nickname)).willReturn(Optional.of(user));
        given(placeRepository.findByPlaceId(kpid)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundApiException.class, () -> {
           sut.saveBookmark(nickname, kpid);
        });
        then(userRepository).should().findByNickname(nickname);
        then(placeRepository).should().findByPlaceId(kpid);

    }

    @DisplayName("DELETE - 북마크 삭제")
    @Test
    public void 북마크_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(bookmarkRepository).deleteById(id);

        // When
        sut.deleteBookmark(id);

        // Then
        then(bookmarkRepository).should().deleteById(id);

    }

}