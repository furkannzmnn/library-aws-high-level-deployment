package com.example.lib.service;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.ErrorCode;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Book;
import com.example.lib.model.Category;
import com.example.lib.model.Image;
import com.example.lib.repository.BookRepository;
import com.example.lib.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BookSaveService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'saveBook_' + #request.userId", value = "bookList"),
            @CacheEvict(value = "bookList", key = "'status' + #request.bookStatus + #request.userId")
    })
    public BookListItemResponse saveBook(SaveBookRequest request) {
        Category category = categoryService.loadCategory(request.getCategoryId());
        final Long userID = userService.findInContextUser().getId();
        final Book book = Book.builder()
                .category(category)
                .bookStatus(request.getBookStatus())
                .title(request.getTitle())
                .publisher(request.getPublisher())
                .lastPageNumber(request.getLastPageNumber())
                .authorName(request.getAuthorName())
                .totalPage(request.getTotalPage())
                .userId(userID)
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

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Transactional
    @Async
    public void saveImage(Long bookId, String imageUrl) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.BOOK_NOT_FOUND).build());
        final Image image = book.getImage();
        if (image == null) {
            book.setImage(imageRepository.save(Image.builder().imageUrl(imageUrl).build()));
        } else {
            image.setImageUrl(imageUrl);
        }
        bookRepository.save(book);
    }

}
