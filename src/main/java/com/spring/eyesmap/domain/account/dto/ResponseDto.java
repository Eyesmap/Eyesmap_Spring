package com.spring.eyesmap.domain.account.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private String exception;

    // success
    public ResponseDto(String message){
        this.message = message;
        this.exception = null;
    }

    // fail
    public ResponseDto(String message, String exception){
        this.message = message;
        this.exception = exception;
    }
}
