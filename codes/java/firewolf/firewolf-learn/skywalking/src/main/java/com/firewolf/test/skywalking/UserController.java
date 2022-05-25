package com.firewolf.test.skywalking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/8
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("get")
    public User findByName(@RequestParam("name") String name) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userService.findByName(name);
    }

    @GetMapping("/baidu")
    public String test(){
        log.info("插叙百度信息");
        String str = restTemplate.getForObject("http://www.baidu.com", String.class);
        return str;
    }
}
