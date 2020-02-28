package com.firewolf.lx.po;

import java.util.Date;

/**
 * Author: liuxing
 * Date: 2020/2/25 14:55
 */
public class BasePO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标志
     */
    private Integer deleted;
}
