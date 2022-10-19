package com.example.lib.api;

import com.example.lib.model.Book;
import com.example.lib.service.BookSaveService;
import com.example.lib.service.ImageStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integration")
class BookImageRestControllerTest {

    public final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BookSaveService bookSaveService;

    @MockBean
    private ImageStoreService imageStoreService;

    @Autowired
    public MockMvc mvc;

    @Test
    void itShouldGetImagePath_WhenValidBookIdAndFile() throws Exception{

        // given - precondition or setup
        String bookId = "1";
        String imagePath = "http://library-folksdev.s3.localhost.localstack.cloud:4566/";
        String baseUrl = String.format(imagePath + "/%s", bookId);

        Long bookIdValue = 1L;

        String fileName = "sample.png";
        MockMultipartFile uploadFile =
                new MockMultipartFile("file", fileName, "image/png", "Some bytes".getBytes());

        // when -  action or the behaviour that we are going test
        when(imageStoreService.uploadImg(convert(uploadFile), bookIdValue)).thenReturn(baseUrl);
        doNothing().when(bookSaveService).saveImage(bookIdValue,baseUrl);

        // then - verify the output
        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/bookImage")
                        .file(uploadFile)
                        .param("bookId", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(baseUrl));

    }

    private File convert(final MultipartFile multipartFile) {
        // convert multipartFile to File
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert multipartFile to File : " + e);
        }
    }
}