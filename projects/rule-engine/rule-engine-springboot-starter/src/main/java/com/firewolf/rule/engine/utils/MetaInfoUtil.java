package com.firewolf.rule.engine.utils;

import com.firewolf.rule.engine.annotations.*;
import com.firewolf.rule.engine.core.EntityMetaInfo;
import com.firewolf.rule.engine.enums.LikeType;
import com.firewolf.rule.engine.enums.OrderType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MetaInfoUtil {

    private final static Map<String, EntityMetaInfo> metaInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取类的元数据信息
     *
     * @param c
     * @return
     */
    public static EntityMetaInfo getMetaInfo(Class<?> c) {
        if (metaInfoMap.containsKey(c.getName())) {
            return metaInfoMap.get(c.getName());
        } else {
            EntityMetaInfo metaInfo = new EntityMetaInfo();
            Table table = c.getDeclaredAnnotation(Table.class);
            String tableName = (table == null || StringUtils.isEmpty(table.value())) ? StringUtils.removeStart(c.getSimpleName().replaceAll("[A-Z]", "_$0").toLowerCase(), "_") : table.value();
            metaInfo.setTable(tableName);
            // 插入主表数据
            Field[] declaredFields = c.getDeclaredFields();
            LinkedHashMap<String, String> columnFieldNameMap = new LinkedHashMap<>();
            Map<String, Field> columnFieldMap = new HashMap<>();
            Map<String, String> filedNameColumnMap = new HashMap<>();
            Map<String, String> orderColumnMap = new HashMap<>();
            Map<String, LikeType> likeColumnMap = new HashMap<>();
            Stream.of(declaredFields).forEach(field -> {
                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    String columnName = StringUtils.removeStart(fieldName.replaceAll("[A-Z]", "_$0").toLowerCase(), "_");

                    if (field.getDeclaredAnnotation(Items.class) == null) {
                        Column annotation = field.getAnnotation(Column.class);
                        if (annotation != null) {
                            columnName = annotation.value();
                        }
                        columnFieldNameMap.put(columnName, fieldName);
                        columnFieldMap.put(columnName, field);
                        filedNameColumnMap.put(fieldName, columnName);
                    } else {
                        if (StringUtils.isEmpty(metaInfo.getItemFieldName())) {
                            metaInfo.setItemFieldName(fieldName);
                            metaInfo.setItemField(field);
                        } else {
                            throw new RuntimeException("@Items can only write on one property");
                        }
                    }

                    if (field.getDeclaredAnnotation(ForeignKey.class) != null) {
                        if (StringUtils.isEmpty(metaInfo.getForeignKeyColumn())) {
                            metaInfo.setForeignKeyColumn(columnName);
                        } else {
                            throw new RuntimeException("@ForeignKey can only write on one property");
                        }
                    }
                    if (field.getDeclaredAnnotation(Id.class) != null) {
                        if (StringUtils.isEmpty(metaInfo.getIdColumn())) {
                            metaInfo.setIdColumn(columnName);
                        } else {
                            throw new RuntimeException("@Id can only write on one property");
                        }
                    }

                    if (field.getDeclaredAnnotation(OrderBy.class) != null) {
                        OrderBy orderBy = field.getDeclaredAnnotation(OrderBy.class);
                        String orderType = orderBy.value() == OrderType.DESC ? "desc" : "asc";
                        orderColumnMap.put(columnName, orderType);
                    }

                    if (field.getDeclaredAnnotation(Like.class) != null) {
                        Like like = field.getDeclaredAnnotation(Like.class);
                        likeColumnMap.put(columnName, like.value());
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            });
            metaInfo.setColumnFieldNameMap(columnFieldNameMap);
            metaInfo.setColumnFieldMap(columnFieldMap);
            metaInfo.setFiledNameColumnMap(filedNameColumnMap);
            metaInfo.setOrderColumnMap(orderColumnMap);
            metaInfo.setLikeColumnMap(likeColumnMap);
            metaInfoMap.put(c.getName(), metaInfo);
            return metaInfo;
        }
    }
}
