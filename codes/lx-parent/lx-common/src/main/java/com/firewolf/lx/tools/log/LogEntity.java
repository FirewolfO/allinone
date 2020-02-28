package com.firewolf.lx.tools.log;

import lombok.Data;

import java.util.Date;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:27
 */
@Data
public class LogEntity {
    private String start;
    private String end;
    private String operator;
    private String method;
    private String parms;
    private String error;
    private String result;
    private Date startTime;
    private Date endTime;
}
