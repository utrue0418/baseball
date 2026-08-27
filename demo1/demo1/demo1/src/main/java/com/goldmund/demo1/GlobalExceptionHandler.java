package com.goldmund.demo1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 💡 "프로젝트 전체에서 발생하는 예외(Exception)를 한 곳에서 모두 잡아냄"
public class GlobalExceptionHandler {

    // 💡 IllegalArgumentException 이라는 에러가 발생하면 무조건 이 메서드가 출동합니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        // 에러가 났던 원래 메시지("해당 종목이 존재하지 않습니다.")를 꺼냅니다.
        String errorMessage = e.getMessage();

        // 서버가 죽는(500) 대신, "네가 잘못된 요청을 한 거야(400)" 라며 친절하게 메시지를 반환합니다.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}