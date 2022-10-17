package com.example.lib.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageStoreService {
    private final AmazonS3 amazonS3;
    private final String BUCKET_NAME = "library-folksdev";
    @Value("${s3.bucket.base.url}")
    private String baseUrl;

    public String uploadImg(File file, Long bookId) {
        amazonS3.putObject(BUCKET_NAME, bookId.toString(), file);
        return baseUrl + bookId;
    }

    public void deleteImg(Long bookId) {
        amazonS3.deleteObject(BUCKET_NAME, bookId.toString());
    }

    public String getImgUrl(Long bookId) {
        return baseUrl + bookId;
    }
}

