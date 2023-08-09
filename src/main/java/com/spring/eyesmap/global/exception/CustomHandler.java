package com.spring.eyesmap.global.exception;

import com.spring.eyesmap.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomHandler {
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ResponseDto> loginFailedException(
            LoginFailedException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("로그인 실패", e.getClass().getName()));
    }
}
