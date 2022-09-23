package com.example.lib.service;

import com.example.lib.dto.BookResponse;
import com.example.lib.dto.CategoryType;
import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import com.example.lib.model.Category;
import com.example.lib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookListService {
    private final CategoryService categoryService;
    private final BookRepository bookRepository;

    // CONVERT REQUEST PARAM
    public List<BookResponse> listBooks(int size, int page) {
       return bookRepository.findAll(PageRequest.of(page,size))
               .getContent()
               .stream()
               .map(BookListService::convertResponse)
               .collect(Collectors.toList());
    }

    public List<BookResponse> searchByCategory(CategoryType categoryType) {
        final Category category = categoryService.findByName(categoryType.getValue());
        return category.getBooks()
                .stream()
                .map(BookListService::convertResponse)
                .collect(Collectors.toList());
    }

    private static BookResponse convertResponse(Book model) {
        return BookResponse.builder()
                .authorName(model.getAuthorName())
                .title(model.getTitle())
                .imageUrl(model.getImage().getImageUrl())
                .build();
    }


    public List<BookResponse> searchBookStatus(BookStatus bookStatus) {
        return bookRepository.findByBookStatus(bookStatus)
                .stream()
                .map(each ->
                    BookResponse.builder()
                            .id(each.getId())
                            .imageUrl(each.getImage().getImageUrl())
                            .build())
                .collect(Collectors.toList());
    }

    public List<BookResponse> searchByTitle(String title) {
        return bookRepository.findByTitle(title)
                .stream()
                .map(each ->
                        BookResponse.builder()
                                .id(each.getId())
                                .imageUrl(each.getImage().getImageUrl())
                                .build())
                .collect(Collectors.toList());
    }
}
