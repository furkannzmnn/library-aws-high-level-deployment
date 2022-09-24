package com.example.lib.dto;

import com.example.lib.model.BookStatus;
import lombok.Data;

import java.io.File;

@Data
public class BookUpdateRequest {
    private Long id;
    private String title;
    private String authorName;
    private BookStatus bookStatus;
    private String publisher;
    private Integer lastPageNumber;
    private File image;
    private Long categoryId;
    private Integer totalPage;
}
