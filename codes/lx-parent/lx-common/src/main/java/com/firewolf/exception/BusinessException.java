package com.firewolf.exception;

import lombok.Data;

/**
 * Author: liuxing
 * Date: 2020/3/3 14:29
 */
@Data
public class BusinessException extends RuntimeException {

    private int errorCode;

    private String errorMsg;

    public BusinessException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
