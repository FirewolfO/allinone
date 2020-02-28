package com.firewolf.lx.web;

import com.firewolf.lx.domain.Response;
import com.firewolf.lx.service.LogService;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/2/28 15:44
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {


    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public Response<List<LogPO>> listLog() {
        List<LogPO> logs = logService.findLogs();
        return Response.ok(logs);
    }
}
