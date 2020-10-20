package com.firewolf.web;

import com.firewolf.bean.Dog;
import com.firewolf.bean.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-22
 */
@RestController
@Api(tags = {"测试项目"})
public class Swagger3Controller implements WebMvcConfigurer {


    @PostMapping
    @ApiOperation(value = "测试方法")
    public Dog findDog(@RequestBody Person person) {
        return Dog.builder().name("TOM").build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
