package com.firewolf.lx.vo;

import com.firewolf.lx.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Author: liuxing
 * Date: 2020/2/25 11:12
 * 用户VO
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO  extends BasePO {

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

    /**
     * 密码确认
     */
    private String confirmPassword;

}
