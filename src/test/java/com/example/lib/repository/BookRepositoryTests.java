package com.example.lib.repository;

import com.example.lib.dto.CategoryType;
import com.example.lib.dto.ErrorCode;
import com.example.lib.exception.GenericException;
import com.example.lib.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class BookRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenBookObject_whenSave_thenReturnSavedBook() {

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title")
                .publisher("Book Publisher")
                .lastPageNumber(100)
                .authorName("Book Author Name")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        // when -  action or the behaviour that we are going test
        Book savedBook = bookRepository.save(book);

        // then - verify the output
        assertThat(savedBook.getId()).isGreaterThan(0);
        assertThat(savedBook).isNotNull();

    }

    @Test
    public void givenBookObject_whenFindById_thenReturnBookObject() {

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title")
                .publisher("Book Publisher")
                .lastPageNumber(100)
                .authorName("Book Author Name")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book savedBook = bookRepository.save(book);

        // when -  action or the behaviour that we are going test
        Book returnedBook = bookRepository.findById(savedBook.getId()).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());

        // then - verify the output
        assertThat(returnedBook).isNotNull();
    }

    @Test
    public void givenBookList_whenFindAll_thenBookList(){

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book1 = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title 1")
                .publisher("Book Publisher 1")
                .lastPageNumber(100)
                .authorName("Book Author Name 1")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book book2 = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title 2")
                .publisher("Book Publisher 2")
                .lastPageNumber(100)
                .authorName("Book Author Name 2")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);

        // when -  action or the behaviour that we are going test
        List<Book> bookListList = bookRepository.findAll();

        // then - verify the output
        assertThat(bookListList).isNotNull();
        assertThat(bookListList.size()).isEqualTo(2);
    }

    @Test
    public void givenBookObject_whenFindByTitle_thenReturnBookObject() {

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title")
                .publisher("Book Publisher")
                .lastPageNumber(100)
                .authorName("Book Author Name")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book savedBook = bookRepository.save(book);

        // when -  action or the behaviour that we are going test
        Book returnedBook = bookRepository.findByTitle("Book Title").orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());

        // then - verify the output
        assertThat(returnedBook).isNotNull();
        assertThat(returnedBook.getTitle()).isEqualTo("Book Title");
    }

    @Test
    public void givenBookObject_whenUpdateBook_thenReturnUpdatedBook(){

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title")
                .publisher("Book Publisher")
                .lastPageNumber(100)
                .authorName("Book Author Name")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book savedBook = bookRepository.save(book);

        Book changedBook = savedBook.toBuilder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title Updated")
                .publisher("Book Publisher Updated")
                .lastPageNumber(105)
                .authorName("Book Author Name Updated")
                .totalPage(115)
                .userId(savedUser.getId())
                .build();

        // when -  action or the behaviour that we are going test
        Book updatedBook =  bookRepository.save(changedBook);

        // then - verify the output
        assertThat(updatedBook.getTitle()).isEqualTo("Book Title Updated");
        assertThat(updatedBook.getPublisher()).isEqualTo("Book Publisher Updated");
        assertThat(updatedBook.getLastPageNumber()).isEqualTo(105);
        assertThat(updatedBook.getAuthorName()).isEqualTo("Book Author Name Updated");
        assertThat(updatedBook.getTotalPage()).isEqualTo(115);
    }

    @Test
    public void givenBookObject_whenDelete_thenRemoveBook(){

        // given - precondition or setup
        User user = User.builder()
                .username("User Name")
                .password("User Password")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(user);

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .category(savedCategory)
                .bookStatus(BookStatus.READ)
                .title("Book Title")
                .publisher("Book Publisher")
                .lastPageNumber(100)
                .authorName("Book Author Name")
                .totalPage(110)
                .userId(savedUser.getId())
                .build();

        Book savedBook = bookRepository.save(book);

        // when -  action or the behaviour that we are going test
        bookRepository.deleteById(savedBook.getId());
        Optional<Book> bookOptional = bookRepository.findById(savedBook.getId());

        // then - verify the output
        assertThat(bookOptional).isEmpty();

    }
}
