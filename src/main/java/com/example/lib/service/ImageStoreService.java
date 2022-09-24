package com.example.lib.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageStoreService {
    private final AmazonS3 amazonS3;
    private final String BUCKET_NAME = "library-folksdev";
    private final String BASE_URL = "https://library-folksdev.s3.eu-west-3.amazonaws.com/";

    public String uploadImg(File file, Long bookId) {
        amazonS3.putObject(BUCKET_NAME, bookId.toString(), file);
        return BASE_URL + bookId;
    }

    public void deleteImg(Long bookId) {
        amazonS3.deleteObject(BUCKET_NAME, bookId.toString());
    }

    public String getImgUrl(Long bookId) {
        return BASE_URL + bookId;
    }
}

