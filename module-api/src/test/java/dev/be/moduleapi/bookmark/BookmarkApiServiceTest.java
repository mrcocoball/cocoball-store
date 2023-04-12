package dev.be.moduleapi.bookmark;

import dev.be.moduleapi.bookmark.dto.BookmarkDto;
import dev.be.moduleapi.bookmark.service.BookmarkApiService;
import dev.be.modulecore.repositories.bookmark.BookmarkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[단일] 북마크 화면 처리 서비스 - 북마크 조회 테스트")
@ExtendWith(MockitoExtension.class)
class BookmarkApiServiceTest {

    @InjectMocks
    private BookmarkApiService sut;

    @Mock
    private BookmarkRepository bookmarkRepository;


    @DisplayName("READ - 북마크 리스트 조회")
    @Test
    public void 북마크_리스트_조회() {

        // Given
        String email = "test";
        Pageable pageable = Pageable.ofSize(10);
        given(bookmarkRepository.findByUser_Email(email, pageable)).willReturn(Page.empty());

        // When
        Page<BookmarkDto> result = sut.getBookmarkList(email, pageable);

        // Then
        assertThat(result).isEmpty();
        then(bookmarkRepository).should().findByUser_Email(email, pageable);

    }

}