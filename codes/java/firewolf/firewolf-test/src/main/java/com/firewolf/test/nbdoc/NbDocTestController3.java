package com.firewolf.test.nbdoc;

import com.firewolf.nbdoc.annotation.ApiClass;
import com.firewolf.nbdoc.annotation.ApiMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:23 上午
 */
@RestController
@ApiClass(description = "NBDoc测试2", groups = {"一级分类"})
@Slf4j
@RequestMapping("/nb/test2")
public class NbDocTestController3 extends BaseWebController {

    @GetMapping
    @ApiMethod(desciption = "简单查询")
    public String test() {
        log.info("aaa");
        return "aaa";
    }

    @PostMapping
    @ApiMethod(desciption = "简单查询2")
    public String test2() {
        log.info("aaa");
        return "aaa";
    }
}
