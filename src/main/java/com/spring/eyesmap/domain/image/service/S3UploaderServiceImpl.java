package com.spring.eyesmap.domain.image.service;


import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.eyesmap.domain.image.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploaderServiceImpl implements S3UploaderService{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<ImageDto.S3UploadResponse> upload(List<MultipartFile> uploadFiles, String dirName) {

        List<ImageDto.S3UploadResponse> imagesResponse = uploadFiles.stream().map((uploadFile) -> {
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
            return new ImageDto.S3UploadResponse(fileName, amazonS3Client.getUrl(bucket, fileName).toString()) ;
        }).toList();
        return imagesResponse;
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
    @Override
    public void deleteFile(String fileName) throws IOException {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }
    @Override
    public String getS3(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

}
