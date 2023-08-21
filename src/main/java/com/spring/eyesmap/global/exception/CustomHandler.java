package com.spring.eyesmap.global.exception;

import com.spring.eyesmap.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomHandler {
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ResponseDto> loginFailedException (
            LoginFailedException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("로그인 실패", e.getClass().getName()));
    }

    @ExceptionHandler(NotFoundAccountException.class)
    public ResponseEntity<ResponseDto> notFoundAccountException (
            NotFoundAccountException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("해당 사용자 없음", e.getClass().getName()));
    }

    @ExceptionHandler(AlreadyReportException.class)
    public ResponseEntity<ResponseDto> AlreadyReportException (
            AlreadyReportException e) {

        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body(new ResponseDto("이미 신고된 신고", e.getClass().getName()));
    }

    @ExceptionHandler(NotFoundReportException.class)
    public ResponseEntity<ResponseDto> NotFoundReportException (
            NotFoundAccountException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("해당 신고 없음", e.getClass().getName()));
    }

    @ExceptionHandler(NotFoundLocationException.class)
    public ResponseEntity<ResponseDto> NotFoundLocationException (
            NotFoundAccountException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("잘못된 주소", e.getClass().getName()));
    }

    @ExceptionHandler(NotFoundDangerousCntException.class)
    public ResponseEntity<ResponseDto> NotFoundDangerousCntException (
            NotFoundDangerousCntException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("해당 신고에 대한 위험해요 공감 없음", e.getClass().getName()));
    }
}
