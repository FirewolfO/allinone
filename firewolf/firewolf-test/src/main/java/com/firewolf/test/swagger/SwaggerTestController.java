package com.firewolf.test.swagger;

import com.firewolf.test.domain.Dog;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/18 8:31 上午
 */

@RestController
@RequestMapping("/test/swagger")
@Api(tags = "安防模块")
public class SwaggerTestController {

    @PostMapping
    @ApiOperation("Hello入口")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "dog", example = "aadadasd")
    })
    public String hello(@RequestBody Dog dog) {
        return "hello, swagger !!!";
    }
}
