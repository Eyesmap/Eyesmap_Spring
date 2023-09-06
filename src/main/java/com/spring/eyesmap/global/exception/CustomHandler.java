package com.spring.eyesmap.global.exception;

import com.spring.eyesmap.global.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomHandler {
    @ExceptionHandler({LoginFailedException.class})
    public BaseResponse<String> loginFailedException (
            LoginFailedException e) {

        return new BaseResponse<>("로그인 실패");
    }

    @ExceptionHandler({NotFoundAccountException.class})
    public BaseResponse<String> notFoundAccountException (
            NotFoundAccountException e) {

        return new BaseResponse<>("계정을 찾을 수 없음");
    }

    @ExceptionHandler({AlreadyReportException.class})
    public BaseResponse<String> AlreadyReportException (
            AlreadyReportException e) {

        return new BaseResponse<>("이미 신고된 신고");
    }
    @ExceptionHandler({AlreadyDeletionReportException.class})
    public BaseResponse<String> AlreadyDeletionReportException (
            AlreadyDeletionReportException e) {

        return new BaseResponse<>("이미 신고된 삭제 신고");
    }

    @ExceptionHandler({NotFoundReportException.class})
    public BaseResponse<String> NotFoundReportException (
            NotFoundReportException e) {

        return new BaseResponse<>("해당 신고 없음");
    }

    @ExceptionHandler({NotFoundLocationException.class})
    public BaseResponse<String> NotFoundLocationException (
            NotFoundLocationException e) {

        return new BaseResponse<>("잘못된 주소");
    }

    @ExceptionHandler({NotFoundDangerousCntException.class})
    public BaseResponse<String> NotFoundDangerousCntException (
            NotFoundDangerousCntException e) {

        return new BaseResponse<>("해당 신고에 대한 위험해요 공감 없음");
    }
    @ExceptionHandler({NotFoundEnumException.class})
    public BaseResponse<String> notFoundEnumException (
            NotFoundEnumException e) {

        return new BaseResponse<>("해당 enum은 존재하지 않음");

    }

    @ExceptionHandler({VoiceOffException.class})
    public BaseResponse<String> voiceOffException (
            VoiceOffException e) {

        return new BaseResponse<>("음성 기능이 꺼져있음");
    }
}