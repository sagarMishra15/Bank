package com.app.Bank.handler;

import com.app.Bank.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger ResponseEntityLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ERRORS = "errors";

    private static final String TAG = "global_exception_handler";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        super.handleExceptionInternal(ex, body, headers, statusCode, request);
        if (ResponseEntityLogger.isErrorEnabled()) {
            ResponseEntityLogger.error(TAG + " : handleExceptionInternal", ex);
            ResponseEntityLogger.error("Error : {}", ex.getMessage());
        }
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        Map<String, List<String>> result = new HashMap<>();
        String result1 = "Invalid input";
        result.put(ERRORS, errors);
        ResponseEntityLogger.info("Error : {}", ex.getMessage());
        return new ResponseEntity<>(result1, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        ResponseEntityLogger.info(TAG + ": handleMethodArgumentNotValid");

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        body.put(ERRORS, errors);
        String se = GsonUtil.gson.toJson(errors);

        ResponseEntityLogger.info(TAG + " : handleMethodArgumentNotValid => {}", se);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
