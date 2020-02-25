package com.firewolf.lx.web;

import com.firewolf.lx.tools.RequestThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: liuxing
 * Date: 2020/2/24 19:59
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class TestController {

    @GetMapping
    public String test(){
        return "hello";
    }

}
