package com.spring.eyesmap.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    public BaseResponse(String exception){
        this.message = "예외 발생";
        this.exception = exception;
    }

}

