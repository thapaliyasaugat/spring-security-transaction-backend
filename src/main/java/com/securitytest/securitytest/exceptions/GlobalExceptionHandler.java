package com.securitytest.securitytest.exceptions;


import com.securitytest.securitytest.resource.ApiResponse;
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
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex){
        ApiResponse response = new ApiResponse();
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
        ApiResponse response = new ApiResponse();
        response.setStatus(1);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException sq){
        ApiResponse response = new ApiResponse();
        response.setStatus(1);
        response.setMessage(sq.getLocalizedMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?> sqlIntegrityConstraintViolationException(UnexpectedTypeException ex){
        ApiResponse response = new ApiResponse();
        response.setStatus(1);
        response.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
