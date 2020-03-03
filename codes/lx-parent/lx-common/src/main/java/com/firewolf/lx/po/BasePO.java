package com.firewolf.lx.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Author: liuxing
 * Date: 2020/2/25 14:55
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePO {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标志
     */
    private Integer deleted;
}
