package com.firewolf.jwt.config.properties;

import lombok.Data;

/**
 * Description:随机秘钥配置
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/23 6:49 上午
 */
@Data
public class RandomSecretKeyConfig {

    /**
     * 密钥生成方式，
     * uuid
     */
    private String method;
}
