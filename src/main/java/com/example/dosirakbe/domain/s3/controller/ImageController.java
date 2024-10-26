package com.example.dosirakbe.domain.s3.controller;



import com.example.dosirakbe.global.config.S3Uploader;
import com.example.dosirakbe.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final S3Uploader s3Uploader;

    // 여러 이미지 업로드
    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> uploadedUrls = s3Uploader.saveFiles(files);
            return new ResponseEntity<>(uploadedUrls, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 단일 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadSingleImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadedUrl = s3Uploader.saveFile(file);
            return new ResponseEntity<>(uploadedUrl, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 이미지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String fileUrl) {
        try {
            s3Uploader.deleteFile(fileUrl);
            return new ResponseEntity<>("Image deleted successfully.", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
