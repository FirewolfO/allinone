package com.firewolf.lx.rule.engine.utils.sql;

import com.firewolf.lx.rule.engine.enums.LikeType;
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
    private Map<String, String> orderBy;


    /**
     * like 字段
     */
    private Map<String, LikeType> like;

    /**
     * limit 信息
     */
    private Limit limit;

}
