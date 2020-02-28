package com.firewolf.lx.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: liuxing
 * Date: 2020/2/25 11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BasePO {


    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}
