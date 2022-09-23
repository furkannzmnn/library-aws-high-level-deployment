package com.example.lib.service;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.model.Book;
import com.example.lib.model.Category;
import com.example.lib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BookSaveService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    @Transactional
    public BookListItemResponse saveBook(SaveBookRequest saveBookRequest) {
        Category category = categoryService.loadCategory(saveBookRequest.getCategoryId());
        final Book book = Book.builder()
                .category(category)
                .bookStatus(saveBookRequest.getBookStatus())
                .title(saveBookRequest.getTitle())
                .publisher(saveBookRequest.getPublisher())
                .lastPageNumber(saveBookRequest.getLastPageNumber())
                .authorName(saveBookRequest.getAuthorName())
                .totalPage(saveBookRequest.getTotalPage())
                .build();

        final Book fromDb = bookRepository.save(book);
        return BookListItemResponse.builder()
                .categoryName(book.getCategory().getName())
                .id(fromDb.getId())
                .bookStatus(fromDb.getBookStatus())
                .publisher(fromDb.getPublisher())
                .authorName(fromDb.getAuthorName())
                .totalPage(fromDb.getTotalPage())
                .lastPageNumber(fromDb.getLastPageNumber())
                .title(fromDb.getTitle())
                .build();
    }




}
