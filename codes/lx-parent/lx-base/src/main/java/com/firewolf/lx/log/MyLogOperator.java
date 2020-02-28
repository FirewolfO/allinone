package com.firewolf.lx.log;

import com.firewolf.lx.tools.log.LogOperator;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/28 12:16
 */
@Component
public class MyLogOperator implements LogOperator {
    @Override
    public String getOperator() {
        return "liuxing";
    }
}
