package com.firewolf.rule.engine.utils.sql;

import com.firewolf.rule.engine.enums.LikeType;
import com.firewolf.rule.engine.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SqlBuildParam {

    /**
     * 表名
     */
    private String table;
    /**
     * 参数
     */
    private Map<String, Object> params;


    /**
     * order by 字段
     */
    private Map<String, OrderType> orderBy;


    /**
     * like 字段
     */
    private Map<String, LikeType> like;

    /**
     * limit 信息
     */
    private Limit limit;

}
