package com.firewolf.test.log;

import com.firewolf.log.annotations.OpLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/4/22
 */
@RestController
@RequestMapping("/log")
@Api("日志")
public class LogTestController {

    @ApiOperation("日志操作测试")
    @PostMapping
    @OpLog(operate = "my dog name is #{user.dog?.name} , again #{user.dog?.name} ， but my name is #{user.name} , my info is #{user}",
            operator = "#{user.name}")
    public String testLog(User user) throws InterruptedException {
        Thread.sleep(2000);
        return "success";
    }
}
