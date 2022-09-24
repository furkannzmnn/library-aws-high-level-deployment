package com.example.lib.service;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.model.Book;
import com.example.lib.model.Category;
import com.example.lib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BookSaveService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    @Transactional


    @Caching(evict = {
            @CacheEvict(key = "'saveBook_' + #request.userId", value = "bookList"),
            @CacheEvict(value = "bookList", key = "'status' + #request.bookStatus + #request.userId")
    })
    public BookListItemResponse saveBook(SaveBookRequest request) {
        Category category = categoryService.loadCategory(request.getCategoryId());
        final Book book = Book.builder()
                .category(category)
                .bookStatus(request.getBookStatus())
                .title(request.getTitle())
                .publisher(request.getPublisher())
                .lastPageNumber(request.getLastPageNumber())
                .authorName(request.getAuthorName())
                .totalPage(request.getTotalPage())
                .userId(request.getUserId())
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
                .userId(fromDb.getUserId())
                .build();
    }


}
