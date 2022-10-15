package com.example.lib.service;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.BookUpdateRequest;
import com.example.lib.dto.ErrorCode;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Book;
import com.example.lib.model.Image;
import com.example.lib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookUpdateService {
    private final ImageStoreService imageStoreService;
    private final BookRepository bookRepository;

    @Transactional(rollbackOn = Exception.class)
    public BookListItemResponse updateBook(BookUpdateRequest updateRequest) {
        final Long id = updateRequest.getId();
        final Book book = bookRepository.findById(id).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.BOOK_NOT_FOUND).build());

        book.setAuthorName(getOrDefault(updateRequest.getAuthorName(), book.getAuthorName()));
        book.setBookStatus(getOrDefault(updateRequest.getBookStatus(), book.getBookStatus()));
        book.setLastPageNumber(getOrDefault(updateRequest.getLastPageNumber(), book.getLastPageNumber()));
        book.setPublisher(getOrDefault(updateRequest.getPublisher(), book.getPublisher()));
        book.setTitle(getOrDefault(updateRequest.getTitle(), book.getTitle()));
        book.setTotalPage(getOrDefault(updateRequest.getTotalPage(), book.getTotalPage()));

        if (updateRequest.getImage() != null) {
            imageStoreService.deleteImg(id);
            book.setImage(new Image(imageStoreService.uploadImg(updateRequest.getImage(), id)));
        }

        bookRepository.save(book);

        return BookListItemResponse.from(book);
    }

    private static <T> T getOrDefault(T data, T defaultValue) {
        return data == null ? defaultValue : data;
    }
}
