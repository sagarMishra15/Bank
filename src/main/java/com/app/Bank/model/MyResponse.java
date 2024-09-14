package com.app.Bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse<T extends Object>{
    private HttpStatus statusCode;
    private String message;
    private T data;

    public static <T> MyResponse<T> newInstance(HttpStatus status, String message) {
        MyResponse<T> response = new MyResponse<>();
        response.statusCode = status;
        response.message = message;
        return response;
    }
}
