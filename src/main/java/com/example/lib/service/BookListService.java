package com.example.lib.service;

import com.example.lib.dto.BookResponse;
import com.example.lib.dto.CategoryType;
import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import com.example.lib.model.Category;
import com.example.lib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookListService {
    private final CategoryService categoryService;
    private final BookRepository bookRepository;

    public List<BookResponse> listBooks(int size, int page, Long userId) {
       return bookRepository.findAll(BookSearchSpecification.searchByUserBooks(userId), PageRequest.of(page,size))
               .getContent()
               .stream()
               .map(BookListService::convertResponse)
               .collect(Collectors.toList());
    }

    @Cacheable(value = "bookList", key = "'saveBook_' + #userId")
    public List<BookResponse> searchByCategory(CategoryType categoryType, Long userId) {
        final Category category = categoryService.findByName(categoryType.getValue());
        return category.getBooks()
                .stream()
                .filter(book -> book.getUserId().equals(userId))
                .map(BookListService::convertResponse)
                .collect(Collectors.toList());
    }

    private static BookResponse convertResponse(Book model) {
        return BookResponse.builder()
                .authorName(model.getAuthorName())
                .title(model.getTitle())
                .imageUrl(model.getImage() != null ? model.getImage().getImageUrl() : null)
                .build();
    }


    @Cacheable(value = "bookList", key = "'status' + #bookStatus + #userId")
    public List<BookResponse> searchBookStatus(BookStatus bookStatus, Long userId) {
        return bookRepository.findAll(BookSearchSpecification.searchByBookStatus(bookStatus, userId ))
                .stream()
                .map(each ->
                    BookResponse.builder()
                            .id(each.getId())
                            .imageUrl(each.getImage() != null ? each.getImage().getImageUrl() : null)
                            .build())
                .collect(Collectors.toList());
    }

    public List<BookResponse> searchByTitle(String title) {
        return bookRepository.findAll(BookSearchSpecification.search(title))
                .stream()
                .map(each ->
                        BookResponse.builder()
                                .id(each.getId())
                                .imageUrl(each.getImage() != null ? each.getImage().getImageUrl() : null)
                                .build())
                .collect(Collectors.toList());
    }

    private static class BookSearchSpecification {
        public static Specification<Book> search(String value) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + value + "%");
        }

        public static Specification<Book> searchByBookStatus(BookStatus bookStatus, Long userId) {
            return (root, query, criteriaBuilder) -> {
                // and query
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("bookStatus"), bookStatus),
                        criteriaBuilder.equal(root.get("userId"), userId)
                );
            };
        }

        public static Specification<Book> searchByUserBooks(Long userId) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
        }
    }
}
