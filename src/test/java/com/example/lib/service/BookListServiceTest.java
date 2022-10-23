package com.example.lib.service;

import com.example.lib.dto.BookResponse;
import com.example.lib.dto.CategoryType;
import com.example.lib.dto.request.SaveBookRequest;
import com.example.lib.model.*;
import com.example.lib.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BookListServiceTest extends BaseServiceTest {

    @InjectMocks
    private BookListService bookListService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;


    @Test
    void givenBookList_whenGetAllBooks_thenReturnBookList() {

        // given - precondition or setup
        BookResponse bookResponse1 = BookResponse.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .build();

        BookResponse bookResponse2 = BookResponse.builder().bookStatus(BookStatus.READING)
                .title("Book Title 2")
                .authorName("Book Author 2")
                .build();

        List<BookResponse> bookListResponse = Arrays.asList(bookResponse1,bookResponse2);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        SaveBookRequest saveBookRequest1 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .userId(1L)
                .categoryId(1L)
                .build();

        SaveBookRequest saveBookRequest2 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 2")
                .authorName("Book Author 2")
                .userId(1L)
                .categoryId(1L)
                .build();

        Book book1 = Book.builder().category(category)
                .bookStatus(saveBookRequest1.getBookStatus())
                .title(saveBookRequest1.getTitle())
                .publisher(saveBookRequest1.getPublisher())
                .lastPageNumber(saveBookRequest1.getLastPageNumber())
                .authorName(saveBookRequest1.getAuthorName())
                .totalPage(saveBookRequest1.getTotalPage())
                .userId(saveBookRequest1.getUserId())
                .build();

        Book book2 = Book.builder().category(category)
                .bookStatus(saveBookRequest2.getBookStatus())
                .title(saveBookRequest2.getTitle())
                .publisher(saveBookRequest2.getPublisher())
                .lastPageNumber(saveBookRequest2.getLastPageNumber())
                .authorName(saveBookRequest2.getAuthorName())
                .totalPage(saveBookRequest2.getTotalPage())
                .userId(saveBookRequest2.getUserId())
                .build();

        // when -  action or the behaviour that we are going test
        when(
                bookRepository.findAll(any(Specification.class), any(Pageable.class))
        ).thenReturn(new PageImpl<Book>(Arrays.asList(book1,book2)));

        // then - verify the output
        List<BookResponse> bookListResponseResult = bookListService.listBooks(2,1,1L);
        assertEquals(bookListResponse.get(0).getAuthorName(), bookListResponseResult.get(0).getAuthorName());
        assertEquals(bookListResponse.get(0).getTitle(), bookListResponseResult.get(0).getTitle());
        assertEquals(bookListResponse.get(1).getAuthorName(), bookListResponseResult.get(1).getAuthorName());
        assertEquals(bookListResponse.get(1).getTitle(), bookListResponseResult.get(1).getTitle());
    }

    @Test
    void givenBookList_whenGetAllBooks_thenSearchByCategory() {

        // given - precondition or setup
        BookResponse bookResponse1 = BookResponse.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .build();

        BookResponse bookResponse2 = BookResponse.builder().bookStatus(BookStatus.READING)
                .title("Book Title 2")
                .authorName("Book Author 2")
                .build();

        List<BookResponse> bookListResponse = Arrays.asList(bookResponse1,bookResponse2);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        SaveBookRequest saveBookRequest1 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .userId(1L)
                .categoryId(1L)
                .build();

        SaveBookRequest saveBookRequest2 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 2")
                .authorName("Book Author 2")
                .userId(1L)
                .categoryId(1L)
                .build();

        Book book1 = Book.builder().category(category)
                .bookStatus(saveBookRequest1.getBookStatus())
                .title(saveBookRequest1.getTitle())
                .publisher(saveBookRequest1.getPublisher())
                .lastPageNumber(saveBookRequest1.getLastPageNumber())
                .authorName(saveBookRequest1.getAuthorName())
                .totalPage(saveBookRequest1.getTotalPage())
                .userId(saveBookRequest1.getUserId())
                .build();

        Book book2 = Book.builder().category(category)
                .bookStatus(saveBookRequest2.getBookStatus())
                .title(saveBookRequest2.getTitle())
                .publisher(saveBookRequest2.getPublisher())
                .lastPageNumber(saveBookRequest2.getLastPageNumber())
                .authorName(saveBookRequest2.getAuthorName())
                .totalPage(saveBookRequest2.getTotalPage())
                .userId(saveBookRequest2.getUserId())
                .build();

        category.setBooks(Arrays.asList(book1,book2));

        // when -  action or the behaviour that we are going test
        when(
                categoryService.findByName(CategoryType.COMIC.getValue())
        ).thenReturn(category);

        // then - verify the output
        List<BookResponse> bookListResponseResult = bookListService.searchByCategory(CategoryType.COMIC,1L);
        assertEquals(bookListResponse.get(0).getTitle(), bookListResponseResult.get(0).getTitle());
        assertEquals(bookListResponse.get(0).getTitle(), bookListResponseResult.get(0).getTitle());
    }

    @Test
    void givenBookList_whenGetAllBooks_thenSearchByTitle() {

        // given - precondition or setup
        BookResponse bookResponse1 = BookResponse.builder().bookStatus(BookStatus.READING)
                .id(1L)
                .imageUrl("Book Image URL 1")
                .title("Book Title 1")
                .authorName("Book Author 1")
                .build();

        BookResponse bookResponse2 = BookResponse.builder().bookStatus(BookStatus.READING)
                .id(2L)
                .imageUrl("Book Image URL 2")
                .title("Book Title 2")
                .authorName("Book Author 2")
                .build();

        List<BookResponse> bookListResponse = Arrays.asList(bookResponse1,bookResponse2);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        SaveBookRequest saveBookRequest1 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title")
                .authorName("Book Author 1")
                .userId(1L)
                .categoryId(1L)
                .build();

        SaveBookRequest saveBookRequest2 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title")
                .authorName("Book Author 2")
                .userId(1L)
                .categoryId(1L)
                .build();

        Book book1 = Book.builder().category(category)
                .bookStatus(saveBookRequest1.getBookStatus())
                .title(saveBookRequest1.getTitle())
                .publisher(saveBookRequest1.getPublisher())
                .lastPageNumber(saveBookRequest1.getLastPageNumber())
                .authorName(saveBookRequest1.getAuthorName())
                .totalPage(saveBookRequest1.getTotalPage())
                .userId(saveBookRequest1.getUserId())
                .build();

        book1.setId(1L);

        Book book2 = Book.builder().category(category)
                .bookStatus(saveBookRequest2.getBookStatus())
                .title(saveBookRequest2.getTitle())
                .publisher(saveBookRequest2.getPublisher())
                .lastPageNumber(saveBookRequest2.getLastPageNumber())
                .authorName(saveBookRequest2.getAuthorName())
                .totalPage(saveBookRequest2.getTotalPage())
                .userId(saveBookRequest2.getUserId())
                .build();

        book2.setId(2L);

        when(
                bookRepository.findAll(any(Specification.class))
        ).thenReturn(Arrays.asList(book1,book2));

        List<BookResponse> bookListResponseResult = bookListService.searchByTitle("Book Title");
        assertEquals(bookListResponse.get(0).getId(), bookListResponseResult.get(0).getId());
        assertEquals(bookListResponse.get(1).getId(), bookListResponseResult.get(1).getId());

    }
    @Test
    void givenBookList_whenGetAllBooks_thenSearchBookStatus() {

        // given - precondition or setup
        BookResponse bookResponse1 = BookResponse.builder().bookStatus(BookStatus.READING)
                .id(1L)
                .build();

        BookResponse bookResponse2 = BookResponse.builder().bookStatus(BookStatus.READING)
                .id(2L)
                .build();

        List<BookResponse> bookListResponse = Arrays.asList(bookResponse1,bookResponse2);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        SaveBookRequest saveBookRequest1 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .userId(1L)
                .categoryId(1L)
                .build();

        SaveBookRequest saveBookRequest2 = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 2")
                .authorName("Book Author 2")
                .userId(1L)
                .categoryId(1L)
                .build();

        Book book1 = Book.builder().category(category)
                .bookStatus(saveBookRequest1.getBookStatus())
                .title(saveBookRequest1.getTitle())
                .publisher(saveBookRequest1.getPublisher())
                .lastPageNumber(saveBookRequest1.getLastPageNumber())
                .authorName(saveBookRequest1.getAuthorName())
                .totalPage(saveBookRequest1.getTotalPage())
                .userId(saveBookRequest1.getUserId())
                .build();

        book1.setId(1L);

        Book book2 = Book.builder().category(category)
                .bookStatus(saveBookRequest2.getBookStatus())
                .title(saveBookRequest2.getTitle())
                .publisher(saveBookRequest2.getPublisher())
                .lastPageNumber(saveBookRequest2.getLastPageNumber())
                .authorName(saveBookRequest2.getAuthorName())
                .totalPage(saveBookRequest2.getTotalPage())
                .userId(saveBookRequest2.getUserId())
                .build();

        book2.setId(2L);

        when(
                bookRepository.findAll(any(Specification.class))
        ).thenReturn(Arrays.asList(book1,book2));

        List<BookResponse> bookListResponseResult = bookListService.searchBookStatus(BookStatus.READING,1L);
        assertEquals(bookListResponse.get(0).getId(), bookListResponseResult.get(0).getId());
        assertEquals(bookListResponse.get(1).getId(), bookListResponseResult.get(1).getId());

    }

    @Test
    void givenBook_whenFindBook_thenBook() {

        // given - precondition or setup
        BookResponse bookResponse = BookResponse.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
                .build();

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        SaveBookRequest saveBookRequest = SaveBookRequest.builder().bookStatus(BookStatus.READING)
                .title("Book Title 1")
                .authorName("Book Author 1")
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

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        BookResponse resultResponse = bookListService.findBook(anyLong());
        assertEquals(resultResponse.getAuthorName(), bookResponse.getAuthorName());
        assertEquals(resultResponse.getTitle(), bookResponse.getTitle());

    }
}
