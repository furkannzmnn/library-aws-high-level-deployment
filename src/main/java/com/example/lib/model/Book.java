package com.example.lib.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int lastPage;
    private BookStatus bookStatus;
    private Long imageId;
    private Long userId;
}
