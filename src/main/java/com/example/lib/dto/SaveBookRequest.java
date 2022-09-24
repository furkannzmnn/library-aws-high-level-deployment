package com.example.lib.dto;

import com.example.lib.model.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
