package com.example.hansumproject.exception.handler;

import com.example.hansumproject.exception.DuplicateDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //시스템 오류 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMessage", "Server error."));
    }

    // 사용자 ID 또는 게스트하우스 ID 유효하지 않을 때의 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMessage", e.getMessage()));
    }

    //ID 중복 예외 처리
    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Object> handleDuplicateUsername(DuplicateDataException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("errorMessage", e.getMessage()));
    }

    // 타입 불일치 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errorMessage", "ID must be a number"));
    }

    // 상태 코드와 함께 발생하는 예외 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("errorMessage", e.getReason() == null ? e.getMessage() : e.getReason()));
    }
}
