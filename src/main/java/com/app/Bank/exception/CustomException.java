package com.app.Bank.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class CustomException extends RuntimeException{
    private final String message;
    private final HttpStatus errorCode;
    private final String status;
    private final Data data;

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CustomException(String message) {
        super(message);
        this.message = message;
        this.errorCode = HttpStatus.BAD_REQUEST;
        this.status = null;
        this.data = null;
    }

    public CustomException(String message, HttpStatus errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = null;
        this.data = null;
    }

    public CustomException(String message, HttpStatus errorCode, String status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.data = null;
    }

    public CustomException(String message, HttpStatus errorCode, String status, Data data) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.data = data;
    }

    class Data implements Serializable {
        private final String message;
        private final String refId;

        public String getMessage() {
            return message;
        }

        public String getRefId() {
            return refId;
        }

        public Data(String message, String refId) {
            this.message = message;
            this.refId = refId;
        }
    }
}
