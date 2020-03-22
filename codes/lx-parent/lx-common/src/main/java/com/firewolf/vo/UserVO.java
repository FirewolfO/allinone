package com.firewolf.vo;

import com.firewolf.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("用户操作封装类")
public class UserVO  extends BaseEntity {

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 密码确认
     */
    @ApiModelProperty("确认密码")
    private String confirmPassword;

}
