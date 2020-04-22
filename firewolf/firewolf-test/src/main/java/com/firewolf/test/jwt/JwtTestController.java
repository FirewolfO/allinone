package com.firewolf.test.jwt;

import com.firewolf.jwt.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/23 7:07 上午
 */
@RestController
@RequestMapping("/jwt")
@Api(tags = "jwt测试")
public class JwtTestController {

    @PostMapping
    @ApiOperation("生成jwtToken")
    public String create(String subject) {
        String token = JwtUtils.createJWT(UUID.randomUUID().toString(), subject, 2000);
        return  token;
    }
}
