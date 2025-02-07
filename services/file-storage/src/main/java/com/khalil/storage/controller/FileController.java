package com.khalil.storage.controller;


import com.khalil.storage.entity.FileMetadata;
import com.khalil.storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/{bucketName}")
    public void createBucket(@PathVariable String bucketName) {
        fileService.createBucket(bucketName);
    }

    @GetMapping
    public List<String> listBuckets() {
        return fileService.listBuckets();
    }

    @DeleteMapping("/{bucketName}")
    public void deleteBucket(@PathVariable String bucketName) {
        fileService.deleteBucket(bucketName);
    }

    @PostMapping(value = "/{bucketName}/objects", consumes = "multipart/form-data")
    public void uploadFile(
            @PathVariable String bucketName,
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadedBy") Integer uploadedBy) throws IOException {
        fileService.uploadFile(bucketName, file, uploadedBy);
    }

    @GetMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String bucketName,
            @PathVariable String objectName) throws IOException {
        byte[] fileBytes = fileService.downloadFile(bucketName, objectName);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + objectName + "\"")
                .body(fileBytes);
    }

    @PatchMapping("/{sourceBucketName}/objects/{objectName}/{targetBucketName}")
    public void moveFile(
            @PathVariable String sourceBucketName,
            @PathVariable String objectName,
            @PathVariable String targetBucketName) {
        fileService.moveFile(sourceBucketName, objectName, targetBucketName);
    }

    @GetMapping("/{bucketName}/objects")
    public List<String> listFiles(@PathVariable String bucketName) {
        return fileService.listFiles(bucketName);
    }

    @GetMapping("/{uploadedBy}/metadata")
    public List<FileMetadata> listFilesByUser(@PathVariable Integer uploadedBy) {
        return fileService.listFilesByUser(uploadedBy);
    }

    @DeleteMapping("/{bucketName}/objects/{objectName}")
    public void deleteFile(@PathVariable String bucketName, @PathVariable String objectName) {
        fileService.deleteFile(bucketName, objectName);
    }

    @DeleteMapping("/{bucketName}/objects")
    public void deleteFiles(@PathVariable String bucketName, @RequestBody List<String> files) {
        fileService.deleteFiles(bucketName, files);
    }
}
