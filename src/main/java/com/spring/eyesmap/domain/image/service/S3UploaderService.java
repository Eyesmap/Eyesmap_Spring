package com.spring.eyesmap.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3UploaderService {
    List<String> upload(List<MultipartFile> multipartFile, String dirName) throws IOException;
}
