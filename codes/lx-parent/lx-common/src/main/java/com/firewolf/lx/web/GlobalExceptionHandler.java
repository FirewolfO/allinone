package com.firewolf.lx.web;

import com.firewolf.lx.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Author: liuxing
 * Date: 2020/2/25 10:19
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 通用的异常处理器
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Response<String> commonErrorHandler(Exception e){

        return null;
    }
}
