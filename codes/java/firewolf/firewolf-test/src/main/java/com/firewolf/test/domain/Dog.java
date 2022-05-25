package com.firewolf.test.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/4/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("狗")
public class Dog {
    @ApiModelProperty(value = "狗名字", example = "TOM")
    private String name;
    @ApiModelProperty(value = "够年龄", example = "12")
    private Integer age;
}
