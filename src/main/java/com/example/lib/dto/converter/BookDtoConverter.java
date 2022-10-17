package com.example.lib.dto.converter;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.SaveBookRequest;
import com.example.lib.model.Book;
import com.example.lib.model.Category;

public class BookDtoConverter {

    public static Book convertToBookDto(SaveBookRequest request, Category category, Long userId) {
        return Book.builder()
                .category(category)
                .bookStatus(request.getBookStatus())
                .title(request.getTitle())
                .publisher(request.getPublisher())
                .lastPageNumber(request.getLastPageNumber())
                .authorName(request.getAuthorName())
                .totalPage(request.getTotalPage())
                .userId(userId)
                .build();
    }

    public static BookListItemResponse toItem(Book fromDb) {
        return BookListItemResponse.builder()
                .categoryName(fromDb.getCategory().getName())
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
