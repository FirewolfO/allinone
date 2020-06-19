package com.firewolf.test.server.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/18
 */
@RestController
@Slf4j
public class EventDataPushController {

    @PostMapping("/event/record/push")
    public Map<String, String> receiveMessage(@RequestBody JSONObject data) {
        log.info("receive message from megvii: {}", data);
        Map<String, String> map = new HashMap<>();
        map.put("result", "success");
        return map;
    }
}
