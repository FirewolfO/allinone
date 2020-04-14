package com.firewolf.entity;

import io.swagger.annotations.ApiModelProperty;
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
public class BaseEntity {
    /**
     * 主键
     */
    @Id
    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private LocalDateTime modifyTime;

    /**
     * 版本号
     */
    @ApiModelProperty(hidden = true)
    private Integer version;

    /**
     * 删除标志
     */
    @ApiModelProperty(hidden = true)
    private Integer deleted;
}