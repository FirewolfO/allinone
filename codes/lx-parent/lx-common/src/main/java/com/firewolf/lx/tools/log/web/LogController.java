package com.firewolf.lx.tools.log.web;

import com.firewolf.lx.domain.Response;
import com.firewolf.lx.tools.log.service.DBLogService;
import com.firewolf.lx.tools.log.service.LogService;
import com.firewolf.lx.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Author: liuxing
 * Date: 2020/2/28 15:44
 * 日志Rest接口，主要用于查询
 */
@RestController
@RequestMapping("/log")
@ConditionalOnProperty(value = "log.rest.enable", havingValue = "true")
public class LogController extends BaseController {


    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public Response<List<Object>> logs() {
        List<Object> list = logService.list();
        return Response.ok(list);
    }
}
