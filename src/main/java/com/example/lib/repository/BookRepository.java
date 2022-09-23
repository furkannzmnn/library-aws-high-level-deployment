package com.example.lib.repository;

import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {
    List<Book> findByBookStatus(BookStatus bookStatus);
    List<Book> findByTitle(String title);
}
