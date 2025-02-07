package com.khalil.storage.service;

import com.khalil.storage.entity.FileMetadata;
import com.khalil.storage.notification.FileUploadedNotificationRequest;
import com.khalil.storage.notification.NotificationProducer;
import com.khalil.storage.repository.FileMetadataRepository;
import com.khalil.storage.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;
    private final FileMetadataRepository fileMetadataRepository;
    private final NotificationProducer notificationProducer;
    private final UserClient userClient;

    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.createBucket(createBucketRequest);
            log.info("Bucket created: {}", createBucketRequest.bucket());
        } catch (S3Exception e) {
            log.error("Error creating bucket: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to create bucket", e);
        }
    }

    public List<String> listBuckets() {
        return s3Client.listBuckets().buckets().stream()
                .map(Bucket::name)
                .toList();
    }

    public void deleteBucket(String bucketName) {
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.deleteBucket(deleteBucketRequest);
            log.info("Bucket deleted: {}", deleteBucketRequest.bucket());
        } catch (S3Exception e) {
            log.error("Error deleting bucket: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to delete bucket", e);
        }
    }

    public void uploadFile(String bucketName, MultipartFile file, Integer uploadedBy) throws IOException {
        var user = this.userClient.findUserById(String.valueOf(uploadedBy))
                .orElseThrow(() -> new RuntimeException("Cannot upload file:: No user exists with the provided ID"));
        String objectKey = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            log.info("File uploaded to the bucket: {}", objectKey);

            FileMetadata metadata = new FileMetadata();

            metadata.setFileId(UUID.randomUUID().toString());
            metadata.setFileName(file.getOriginalFilename());
            metadata.setFileSize(file.getSize());
            metadata.setFileType(file.getContentType());
            metadata.setBucketName(bucketName);
            metadata.setObjectKey(objectKey);
            metadata.setUploadedBy(uploadedBy);
            metadata.setUploadDate(new Date());

            fileMetadataRepository.save(metadata);
            log.info("File metadata saved !");

            this.notificationProducer.sendNotification(
                    new FileUploadedNotificationRequest(
                            metadata.getFileName(),
                            user
                    )
            );

        } catch (S3Exception e) {
            log.error("Error uploading file: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadFile(String bucketName, String objectName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        try {
            return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
        } catch (S3Exception e) {
            log.error("Error downloading file: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public List<String> listFiles(String bucketName) {
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            return s3Client.listObjects(listObjects).contents().stream()
                    .map(S3Object::key)
                    .toList();
        } catch (S3Exception e) {
            log.error("Error listing files: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to list files", e);
        }
    }

    public List<FileMetadata> listFilesByUser(Integer uploadedBy) {
        return fileMetadataRepository.findByUploadedBy(uploadedBy);
    }

    public void deleteFile(String bucketName, String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted from the bucket: {}", objectName);

            fileMetadataRepository.deleteByObjectKey(objectName);
            log.info("File metadata deleted !");
        } catch (S3Exception e) {
            log.error("Error deleting file: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public void deleteFiles(String bucketName, List<String> files) {
        ArrayList<ObjectIdentifier> objectIdentifiers = new ArrayList<>();

        files.forEach(file -> {
            ObjectIdentifier objectIdentifier = ObjectIdentifier.builder()
                    .key(file)
                    .build();

            objectIdentifiers.add(objectIdentifier);
        });

        Delete delete = Delete.builder()
                .objects(objectIdentifiers)
                .build();

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(delete)
                .build();

        try {
            s3Client.deleteObjects(deleteObjectsRequest);
            log.info("Files deleted: {}", objectIdentifiers.stream()
                    .map(ObjectIdentifier::key)
                    .collect(Collectors.joining()));
        } catch (S3Exception e) {
            log.error("Error deleting files: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to delete files", e);
        }
    }

    public void moveFile(String sourceBucketName, String objectName, String targetBucketName) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(sourceBucketName)
                .sourceKey(objectName)
                .destinationBucket(targetBucketName)
                .destinationKey(objectName)
                .build();

        try {
            CopyObjectResponse copyObjectResponse = s3Client.copyObject(copyObjectRequest);
            if (copyObjectResponse != null) {
                deleteFile(sourceBucketName, objectName);
                log.info("File moved and deleted: {}", copyObjectRequest.sourceKey());
            }
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to move file", e);
        }
    }
}