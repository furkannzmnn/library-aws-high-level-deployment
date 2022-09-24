package com.example.lib.api;

import com.example.lib.service.BookSaveService;
import com.example.lib.service.ImageStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/bookImage")
@RequiredArgsConstructor
public class BookImageRestController {
    private final BookSaveService bookSaveService;
    private final ImageStoreService imageStoreService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("bookId") Long bookId, @RequestParam("file") MultipartFile file) {
        final String uploadImg = imageStoreService.uploadImg(convert(file), bookId);
        bookSaveService.saveImage(bookId, uploadImg);
        return ResponseEntity.ok(uploadImg);
    }

    private File convert(final MultipartFile multipartFile) {
       // convert multipartFile to File
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
