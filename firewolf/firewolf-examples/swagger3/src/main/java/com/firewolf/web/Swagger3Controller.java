package com.firewolf.web;

import com.firewolf.bean.Dog;
import com.firewolf.bean.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-22
 */
@RestController
@Tag(name = "测试swagger")
public class Swagger3Controller {


    @PostMapping
    @Operation(description = "查询狗狗", parameters = {
            @Parameter(name = "person",examples = {
                    @ExampleObject(name = "name",value = "zhangsan"),
                    @ExampleObject(name = "zoneId",value = "100")
            })
    })
    public Dog findDog(@RequestBody Person person) {
        return Dog.builder().name("TOM").build();
    }
}
