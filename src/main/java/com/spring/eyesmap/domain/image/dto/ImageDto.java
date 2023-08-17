package com.spring.eyesmap.domain.image.dto;

import lombok.Data;
import lombok.Getter;

public class ImageDto {
    @Getter
    public static class S3UploadResponse{
        String imgUrl;
        String imgFileNm;

        public S3UploadResponse(String imgFileNm, String imgUrl) {
            this.imgUrl = imgUrl;
            this.imgFileNm = imgFileNm;
        }
    }
}
