package com.example.lib.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageStoreService {
    private final String BUCKET_NAME = "library-folksdev";
    private final String BASE_URL = "https://library-folksdev.s3.eu-west-3.amazonaws.com/";

    private final AmazonS3 amazonS3;

    public void storeImage(String key, File file) {
        String name = file.getName();
        name = key + "_" + name;
        amazonS3.putObject(BUCKET_NAME,name,file);
    }

    public void storeImage(String key, InputStream inputStream) {
        amazonS3.putObject(BUCKET_NAME,key,inputStream,null);
    }

    public void deleteImage(String key) {
        amazonS3.deleteObject(BUCKET_NAME,key);
    }


    // list all images
    public void listImages() {
        amazonS3.listObjects(BUCKET_NAME).getObjectSummaries().forEach(s3ObjectSummary -> {
            System.out.println(s3ObjectSummary.getKey());
        });
    }

    // list all images with key
    public String listImagesWithKey(String key) {
        List<String> url = new ArrayList<>();
        amazonS3.listObjects(BUCKET_NAME,key).getObjectSummaries().forEach(s3ObjectSummary -> {
            url.add(BASE_URL + s3ObjectSummary.getKey());
        });
        return url.iterator().next();
    }


}

