package com.app.Bank.jwt_config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class LoggerFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
        contentCachingResponseWrapper.copyBodyToResponse();
        long timeTaken = System.currentTimeMillis() - startTime;
        String requestBody = getStringValue(contentCachingRequestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(contentCachingResponseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
        if(request.getRequestURI().contains("/auth/login/")){
            LOGGER.info("Filter Logs : METHOD = {};  REQUEST URI = {}; REQUEST BODY = HIDDEN; RESPONSE CODE= {}; RESPONSE BODY = {}; TIME TAKEN = {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), responseBody, timeTaken);
        }else {
            LOGGER.info("Filter Logs : METHOD = {};  REQUEST URI = {}; REQUEST BODY = {}; RESPONSE CODE= {}; RESPONSE BODY = {}; TIME TAKEN = {}",
                    request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody, timeTaken);
        }
        contentCachingResponseWrapper.copyBodyToResponse();

    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {


        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
