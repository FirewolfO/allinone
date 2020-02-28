package com.firewolf.lx.domain;

import lombok.Data;
/**
 * Author: liuxing
 * Date: 2020/2/24 20:49
 * Web请求返回结果数据
 */
@Data
public class Response<T> {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 结构状态码
     */
    private Integer code;

    /**
     * 结果描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response ok(T data) {
        return null;
    }
}
