package com.example.lib.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
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


    // list all images with key
    public String listImagesWithKey(String key) {
        List<String> url = new ArrayList<>();
        amazonS3.listObjects(BUCKET_NAME,key).getObjectSummaries().forEach(s3ObjectSummary -> {
            url.add(BASE_URL + s3ObjectSummary.getKey());
        });
        return url.iterator().next();
    }


}

