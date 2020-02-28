package com.firewolf.lx.web;

import com.firewolf.lx.domain.Response;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:20
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public Response<String> test() {
        List<LogPO> logPOS = jdbcTemplate.query("select * from lx_log", new BeanPropertyRowMapper<>(LogPO.class));
        return Response.ok(logPOS);
    }
}
