package com.firewolf.jwt.entity;

import io.jsonwebtoken.Claims;
import lombok.Data;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/23 6:37 上午
 */
@Data
public class CheckResult {

    /**
     * 是否成功
     */
    private  boolean success;

    /**
     * 错误码
     */
    private int errCode;

    /**
     *
     */
    private Claims claims;
}
