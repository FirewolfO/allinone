package com.firewolf.test.nbdoc;

import com.firewolf.nbdoc.annotation.ApiClass;
import com.firewolf.nbdoc.annotation.ApiMethod;
import com.firewolf.test.domain.Dog;
import com.firewolf.test.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:23 上午
 */
@RestController
@ApiClass(description = "NBDoc测试", groups = {"一级分类"})
@Slf4j
public class NbDocTestController {

    @PostMapping("/test")
    @ApiMethod(desciption = "简单查询")
    public Dog test(@RequestBody User user) {
        log.info("aaa");
        return new Dog("TOM", 5);
    }
}
