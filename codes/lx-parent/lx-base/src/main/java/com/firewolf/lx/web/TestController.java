package com.firewolf.lx.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: liuxing
 * Date: 2020/2/24 19:59
 */
@RestController
@RequestMapping("/hello")
public class TestController {

    @GetMapping
    public String test(){
        return "hello";
    }

}
