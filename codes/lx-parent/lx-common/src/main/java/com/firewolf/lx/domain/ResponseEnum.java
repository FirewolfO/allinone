package com.firewolf.lx.domain;

/**
 * Author: liuxing
 * Date: 2020/2/25 10:25
 * 返回结构枚举枚举
 */
public enum ResponseEnum {

    OK(200,"LX_RESPONSE_001"), // 成功
    FAILED(500,"LX_RESPONSE_002") // 服务器内部错误
    ;


    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 描述
     */
    private String msg;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
