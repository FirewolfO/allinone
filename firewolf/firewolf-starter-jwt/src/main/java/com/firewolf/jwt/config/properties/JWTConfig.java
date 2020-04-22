package com.firewolf.jwt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/23 6:45 上午
 */
@ConfigurationProperties(prefix = "firewolf.jwt")
@Data
public class JWTConfig {

    /**
     * 密钥类型，
     * 可选值：flexd->固定的，random->随机的，custom
     * 如果用户自己注入了
     */
    private String secretType;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 随机密钥配置
     */
    private RandomSecretKeyConfig random;
}
