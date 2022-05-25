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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dog {
    @ApiModelProperty(example = "TOM")
    private String name;
    @ApiModelProperty(example = "22")
    private Integer age;
}
