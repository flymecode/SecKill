package com.xupt.seckill.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    public ResponseEntity exceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof Exception) {

        }
        return null;
    }
}
