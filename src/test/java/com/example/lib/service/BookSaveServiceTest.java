package com.example.lib.service;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.CategoryType;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.dto.UserDto;
import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import com.example.lib.model.Category;
import com.example.lib.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class BookSaveServiceTest extends BaseServiceTest {

    @InjectMocks
    private BookSaveService bookSaveService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private UserService userService;
    @Mock
    private CacheClient cacheClient;


    @Test
    void shouldReturnBookListItemResponseWhenNewSaveBook() {
        // given

        BookListItemResponse.builder().bookStatus(BookStatus.READING)
                .title("title")
                .userId(1L)
                .categoryName(CategoryType.COMIC.name())
                .build();
        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        final SaveBookRequest saveBookRequest = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("title")
                .userId(1L)
                .categoryId(1L)
                .build();

        Book book = Book.builder().category(category)
                .bookStatus(saveBookRequest.getBookStatus())
                .title(saveBookRequest.getTitle())
                .publisher(saveBookRequest.getPublisher())
                .lastPageNumber(saveBookRequest.getLastPageNumber())
                .authorName(saveBookRequest.getAuthorName())
                .totalPage(saveBookRequest.getTotalPage())
                .userId(saveBookRequest.getUserId())
                .build();

        // when
        when(categoryService.loadCategory(anyLong())).thenReturn(category);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(userService.findInContextUser()).thenReturn(new UserDto());

        // then
        BookListItemResponse bookListItemResponse = bookSaveService.saveBook(saveBookRequest);
        assertEquals(BookStatus.READING, bookListItemResponse.getBookStatus());
        assertEquals("title", bookListItemResponse.getTitle());
        assertEquals(1L, bookListItemResponse.getUserId());

    }
}