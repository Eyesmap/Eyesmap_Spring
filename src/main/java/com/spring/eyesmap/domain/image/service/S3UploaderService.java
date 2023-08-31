package com.spring.eyesmap.domain.image.service;

import com.spring.eyesmap.domain.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3UploaderService {
    List<ImageDto.S3UploadResponse> upload(List<MultipartFile> multipartFile, String dirName) throws IOException;
    void deleteFile(String fileName) throws IOException;
    String getS3(String fileName);
}
