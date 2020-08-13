package com.firewolf.test.nbdoc;

import com.firewolf.nbdoc.annotation.ApiClass;
import com.firewolf.nbdoc.annotation.ApiMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:23 上午
 */
@RestController
@ApiClass(description = "NBDoc测试2", groups = {"一级分类","二级分类"})
@Slf4j
@RequestMapping("/nb/test2")
public class NbDocTestController2 extends BaseWebController {

    @GetMapping("/test")
    @ApiMethod(desciption = "简单查询")
    public String test() {
        log.info("aaa");
        return "aaa";
    }
}
