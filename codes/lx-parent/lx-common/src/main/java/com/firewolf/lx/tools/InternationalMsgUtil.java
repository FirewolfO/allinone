package com.firewolf.lx.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/25 10:24
 * 国际化处理工具
 */
@Component
@Slf4j
public class InternationalMsgUtil {


    /**
     * 根据key获取国际化结果
     *
     * @param key
     * @return
     */
    public String getMsg(String key) {
        return key;
    }

}
