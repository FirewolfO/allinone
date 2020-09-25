package com.firewolf.bean;

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
    private String name;
    private Integer zoneId;
}
