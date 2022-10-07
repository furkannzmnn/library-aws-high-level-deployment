package com.example.lib.repository;

import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {

    Optional<Book> findByTitle(String title);
}
