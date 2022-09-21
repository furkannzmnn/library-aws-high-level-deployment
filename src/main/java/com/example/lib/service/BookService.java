package com.example.lib.service;

import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    public static final String OPENLIBRARY_SEARCH_REQUEST = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    public static final String ISBN_OR_QUERY = "https://openlibrary.org/search.json?q=isbn:";
    private static final String OPENLIBRARY_SEARCH_REQUEST_SUFFIX = "&jscmd=data&format=json";
    private static final String IMAGE_URL = "https://covers.openlibrary.org/b/isbn/";
    private static final String IMAGE_URL_SUFFIX = "-L.jpg";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BookService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<String> saveNewBook() {
        Book book = Book.builder()
                .bookStatus(BookStatus.BORROWED)
                .title("The Lord of the Rings")
                .author("J.R.R. Tolkien")
                .isbn("978-975-8725-71-7")
                .build();
        if (hasISBN(book)) {
            return getBook(book.getIsbn());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<String> getBook(String isbn) {
        LinkedHashMap<String, Object> response = restTemplate.getForObject(OPENLIBRARY_SEARCH_REQUEST + isbn + OPENLIBRARY_SEARCH_REQUEST_SUFFIX, LinkedHashMap.class);
        if (response != null) {
            LinkedHashMap<String, Object> book = (LinkedHashMap<String, Object>) response.get("ISBN:" + isbn);
            return Optional.ofNullable(book)
                    .map(this::convertResponse)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found");
    }

    private ResponseEntity<String> convertResponse(LinkedHashMap<String, Object> book) {
        try {
            final BookResponse response = objectMapper.convertValue(book, BookResponse.class);
            return ResponseEntity.ok(response.getCover().getLarge());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BookResponse {
        private String url;
        private String key;
        private String title;
        private List<Author> authors;
        private float number_of_pages;
        @JsonIgnore
        private Identifiers identifiers;
        @JsonIgnore
        private List<Object> publishers = new ArrayList<>();
        private String publish_date;
        private String notes;
        private Cover cover;
    }


    private boolean hasISBN(Book book) {
        return book.getIsbn() != null;
    }
}

@RequestMapping("/books")
@RestController
class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<String> getBooks() {
        return bookService.saveNewBook();
    }
}
