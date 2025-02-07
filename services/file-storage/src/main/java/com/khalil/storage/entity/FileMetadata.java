package com.khalil.storage.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "files")
public class FileMetadata {
    @Id
    private String fileId;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String bucketName;
    private String objectKey;
    private Integer uploadedBy;
    private Date uploadDate;
    private List<String> tags;
    private String description;
}
