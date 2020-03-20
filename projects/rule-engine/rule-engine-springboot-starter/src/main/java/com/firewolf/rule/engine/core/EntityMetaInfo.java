package com.firewolf.rule.engine.core;

import com.firewolf.rule.engine.enums.LikeType;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
     * item字段
     */
    private Field itemField;

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
     * 数据库列和字段的对应关系
     */
    private Map<String, Field> columnFieldMap = new HashMap<>();

    /**
     * 排序字段
     */
    private Map<String, String> orderColumnMap = new HashMap<>();

    /**
     * 带有like语句的字段
     */
    private Map<String, LikeType> likeColumnMap = new HashMap<>();

}