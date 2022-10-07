package com.example.lib.service;

import com.example.lib.dto.BookResponse;
import com.example.lib.dto.CategoryType;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.model.*;
import com.example.lib.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookListServiceTest {

    @InjectMocks
    private BookListService bookListService;

    @Mock
    private BookRepository bookRepository;


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

}
