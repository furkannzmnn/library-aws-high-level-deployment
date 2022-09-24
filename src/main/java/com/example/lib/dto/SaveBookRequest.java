package com.example.lib.dto;

import com.example.lib.model.BookStatus;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

@Data
public final class SaveBookRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String authorName;
    @NotNull
    private BookStatus bookStatus;
    @NotBlank
    private String publisher;
    @NotNull
    private Integer lastPageNumber;
    private File image;
    @NotNull
    private Long categoryId;
    @NotNull
    private Integer totalPage;
    private Long userId;
}
