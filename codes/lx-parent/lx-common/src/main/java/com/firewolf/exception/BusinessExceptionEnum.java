package com.firewolf.exception;

/**
 * Author: liuxing
 * Date: 2020/3/3 14:34
 */
public enum  BusinessExceptionEnum {
    COMMON_TRANSDTO(500,"LX_COMMON_INNER_ERROR") // 服务器内部错误
    ;
    private int code;
    private String msg;
    BusinessExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
