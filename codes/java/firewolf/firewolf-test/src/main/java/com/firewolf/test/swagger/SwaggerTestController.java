package com.firewolf.test.swagger;

import com.firewolf.test.domain.Dog;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    @ApiOperation(value = "Hello入口")
    public String hello(@RequestParam("file")
                            @ApiParam(name = "file",
                                    value = "上传图片文件, 最大10m,支持格式:jpg|jpeg|bmp|png|JPG|JPEG|BMP|PNG",
                                    required = true) MultipartFile file) {
        return "hello, swagger !!!";
    }
}
