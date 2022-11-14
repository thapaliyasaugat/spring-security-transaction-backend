package com.securitytest.securitytest.exceptions;


import com.securitytest.securitytest.resource.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex){
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(1);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        Map<String,String> resp= new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName,message);
        });
        return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runTimeException(RuntimeException ex){
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(1);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException sq){
        log.error(sq.getMessage());
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(1);
        response.setMessage("Duplicate Entry or some other SQL violations.");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?> sqlIntegrityConstraintViolationException(UnexpectedTypeException ex){
        log.error(ex.getMessage());
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(1);
        response.setMessage("Unexpected type, check all constraints.");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwtException(ExpiredJwtException ex){
        log.error(" expired jwt exception : {}",ex.getMessage());
        return new ResponseEntity<>(new ApiResponse<String>(null,"Session Expired.",1),HttpStatus.BAD_REQUEST);
    }
}
