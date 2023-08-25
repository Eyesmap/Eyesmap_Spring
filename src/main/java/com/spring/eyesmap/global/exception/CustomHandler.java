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

    @ExceptionHandler(AlreadyReportException.class)
    public BaseResponse<Void> AlreadyReportException (
            AlreadyReportException e) {

        return new BaseResponse<>("이미 신고된 신고");
    }

    @ExceptionHandler(NotFoundReportException.class)
    public BaseResponse<Void> NotFoundReportException (
            NotFoundAccountException e) {

        return new BaseResponse<>("해당 신고 없음");
    }

    @ExceptionHandler(NotFoundLocationException.class)
    public BaseResponse<Void> NotFoundLocationException (
            NotFoundAccountException e) {

        return new BaseResponse<>("잘못된 주소");
    }

    @ExceptionHandler(NotFoundDangerousCntException.class)
    public BaseResponse<Void> NotFoundDangerousCntException (
            NotFoundDangerousCntException e) {

        return new BaseResponse<>("해당 신고에 대한 위험해요 공감 없음");
    }
}
