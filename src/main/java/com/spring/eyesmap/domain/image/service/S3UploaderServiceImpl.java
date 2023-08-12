package com.spring.eyesmap.domain.image.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploaderServiceImpl implements S3UploaderService{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> upload(List<MultipartFile> uploadFiles, String dirName) {

        List<String> imageUrls = uploadFiles.stream().map((uploadFile) -> {
            String fileName = createStoreFileName(dirName, uploadFile.getOriginalFilename());
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(uploadFile.getContentType());
            objectMetaData.setContentLength(uploadFile.getSize());

            try {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, fileName, uploadFile.getInputStream(), objectMetaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return amazonS3Client.getUrl(bucket, fileName).toString();
        }).toList();
        return imageUrls;
    }

    private String createStoreFileName(String dirName, String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return dirName + "/" + uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
