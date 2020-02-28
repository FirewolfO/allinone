package com.firewolf.lx.web;

/**
 * Author: liuxing
 * Date: 2020/2/24 20:45
 */

import com.firewolf.lx.tools.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户处理器
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {


    @GetMapping
    @Log(operate = "test")
    public String test() {
        return "hello";
    }

}
