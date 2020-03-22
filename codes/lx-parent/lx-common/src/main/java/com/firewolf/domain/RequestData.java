package com.firewolf.domain;

import lombok.Data;

/**
 * Author: liuxing
 * Date: 2020/2/24 21:15
 */
@Data
public class RequestData {
    /**
     * 请求ID
     */
    private String requestId;

    /**
     *  语言信息
     */
    private String lang;
}
