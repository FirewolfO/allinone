package com.firewolf.rule.engine.utils.sql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Limit {

    /**
     * 开始位置
     */
    private int start = -1;
    /**
     * 查询条数
     */
    private int size;

}
