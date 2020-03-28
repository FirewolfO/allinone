package com.firewolf.rule.engine.entity;

import com.firewolf.rule.engine.enums.LikeType;
import com.firewolf.rule.engine.enums.OrderType;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体对象元数据属性
 */
@Data
public class EntityMetaInfo {

    /**
     * 表名
     */
    private String table;

    /**
     * ID字段
     */
    private String idColumn;

    /**
     * item字段
     */
    private String itemFieldName;

    /**
     * 外键字段
     */
    private String foreignKeyColumn;

    /**
     * 数据库列--Java属性对应关系
     */
    private LinkedHashMap<String, String> columnFieldNameMap = new LinkedHashMap<>();

    /**
     * Java属性---数据库列对应关系
     */
    private Map<String, String> filedNameColumnMap = new HashMap<>();

    /**
     * 排序字段
     */
    private Map<String, OrderType> orderColumnMap = new HashMap<>();

    /**
     * 带有like语句的字段
     */
    private Map<String, LikeType> likeColumnMap = new HashMap<>();

    /**
     * 唯一约束，只要一个出现，就认为冲突了
     */
    private List<String> uniqueKeys = new ArrayList<>();

    /**
     * 组合主键，组合在一起入股出现了，就认为冲突了
     */
    private List<String> unionKeys = new ArrayList<>();

}
