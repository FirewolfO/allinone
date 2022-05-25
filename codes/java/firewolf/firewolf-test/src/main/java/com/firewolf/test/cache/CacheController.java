package com.firewolf.test.cache;

import com.firewolf.cache.LocalCache;
import com.firewolf.test.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-08-05
 */
@RestController
@RequestMapping("/test/cache")
@Api(tags = "缓存测试")
public class CacheController {

    @PostMapping
    @LocalCache(desc = "用户缓存", key = "#user.name")
    @ApiOperation("测试缓存添加")
    public String testAdd(User user) {
        System.out.println(user);
        return user.getName();
    }
}
