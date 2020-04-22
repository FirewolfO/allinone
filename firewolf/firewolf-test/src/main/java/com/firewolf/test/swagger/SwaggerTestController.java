package com.firewolf.test.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/18 8:31 上午
 */

@RestController
@RequestMapping("/test/swagger")
@Api(tags = "Swagger测试接口")
public class SwaggerTestController {

    @GetMapping
    @ApiOperation("Hello入口")
    public String hello(){
        return "hello, swagger !!!";
    }
}
