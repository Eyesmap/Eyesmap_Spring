package com.spring.eyesmap.global.exception;

import com.spring.eyesmap.global.response.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomHandler {
    @ExceptionHandler(LoginFailedException.class)
    public BaseResponse<Void> loginFailedException (
            LoginFailedException e) {

        return new BaseResponse<>("로그인 실패");
    }

    @ExceptionHandler(NotFoundAccountException.class)
    public BaseResponse<Void> notFoundAccountException (
            NotFoundAccountException e) {

        return new BaseResponse<>("계정을 찾을 수 없음");
    }

    @ExceptionHandler(NotFoundEnumException.class)
    public BaseResponse<Void> notFoundEnumException (
            NotFoundEnumException e) {

        return new BaseResponse<>("해당 enum은 존재하지 않음");
    }
}
