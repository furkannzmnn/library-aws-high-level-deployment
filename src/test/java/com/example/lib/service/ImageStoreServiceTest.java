package com.example.lib.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageStoreServiceTest extends BaseServiceTest {

    @InjectMocks
    ImageStoreService imageStoreService;

    @Mock
    AmazonS3 amazonS3;

    private final String BUCKET_NAME = "library-folksdev";

    @Value("${s3.bucket.base.url}")
    private String baseUrl;

    @Test
    void givenBookIdAndFile_thenReturnBaseUrlOfImage() {

        // given - precondition or setup
        Long bookId = 1L;
        String fileName = "sample.png";
        MockMultipartFile uploadFile =
                new MockMultipartFile("file", fileName, "image/png", "Some bytes".getBytes());

        File file = convert(uploadFile);

        // when -  action or the behaviour that we are going test
        when(amazonS3.putObject(BUCKET_NAME,bookId.toString(), file)).thenReturn(new PutObjectResult());

        // then - verify the output
        imageStoreService.uploadImg(file,bookId);

        verify(amazonS3, times(1)).putObject(BUCKET_NAME,bookId.toString(), file);
    }

    @Test
    void givenBookId_thenDeleteImageOfBook() {

        // given - precondition or setup
        Long bookId = 1L;

        // when -  action or the behaviour that we are going test
        doNothing().when(amazonS3).deleteObject(BUCKET_NAME,bookId.toString());

        // then - verify the output
        imageStoreService.deleteImg(1L);

        verify(amazonS3, times(1)).deleteObject(BUCKET_NAME,bookId.toString());
    }

    @Test
    void givenBookId_tenReturnBaseUrlOfImage() {

        // given - precondition or setup
        Long bookId = 1L;

        String expected = baseUrl + bookId;

        // when -  action or the behaviour that we are going test

        // then - verify the output
        String result = imageStoreService.getImgUrl(1L);

        assertEquals(expected, result);

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