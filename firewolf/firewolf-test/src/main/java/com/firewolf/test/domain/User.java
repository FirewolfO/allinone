package com.firewolf.test.domain;

import io.swagger.annotations.ApiModel;
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
@ApiModel("用户")
public class User {
    private String name;
    private Integer age;

    private Dog dog;
}
