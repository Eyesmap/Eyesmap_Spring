package com.spring.eyesmap.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.spring.eyesmap.global.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"message", "exception", "result"})
public class BaseResponse<T> {
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse() {
        this.message = "성공했습니다.";
    }

    public BaseResponse(T result) {
        this.message = "성공했습니다.";
        this.result = result;
    }

    public BaseResponse(ResponseDto responseDto) {
        this.message = responseDto.getMessage();
        this.exception = responseDto.getException();
    }

    public BaseResponse(ResponseDto responseDto, T result) {
        this.message = responseDto.getMessage();
        this.exception = responseDto.getException();
        this.result = result;
    }

}

