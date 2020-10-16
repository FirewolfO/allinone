package com.firewolf.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    @ApiModelProperty(example = "张三")
    private String name;
    @ApiModelProperty(example = "12")
    private Integer zoneId;
}
