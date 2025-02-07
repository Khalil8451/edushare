package com.khalil.storage.repository;

import com.khalil.storage.entity.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    List<FileMetadata> findByUploadedBy(Integer uploadedBy);
    void deleteByObjectKey(String objectKey);
}