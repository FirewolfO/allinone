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
            Map<String, String> filedNameColumnMap = new HashMap<>();
            Map<String, OrderType> orderColumnMap = new HashMap<>();
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
                        filedNameColumnMap.put(fieldName, columnName);
                    } else {
                        if (StringUtils.isEmpty(metaInfo.getItemFieldName())) {
                            metaInfo.setItemFieldName(fieldName);
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
                        orderColumnMap.put(columnName, orderBy.value());
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
            metaInfo.setFiledNameColumnMap(filedNameColumnMap);
            metaInfo.setOrderColumnMap(orderColumnMap);
            metaInfo.setLikeColumnMap(likeColumnMap);
            metaInfoMap.put(c.getName(), metaInfo);
            return metaInfo;
        }
    }


    /**
     * 获取数据对象的属性值，去掉控制属性,把属性转成了数据库字段
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMapNoNull(Object obj) {
        return getColumnFieldValueMap(obj, false);
    }


    /**
     * 获取对象的属性值，保留空值属性,把属性转成了数据库字段
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj) {
        return getColumnFieldValueMap(obj, true);
    }

    /**
     * 获取某个对象的列->值的集合
     *
     * @param obj      数据对象
     * @param withNull 是否要空值的数据
     * @return
     */
    private static Map<String, Object> getColumnFieldValueMap(Object obj, boolean withNull) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (obj != null) {
                Class<?> clazz = obj.getClass();
                Map<String, String> filedNameColumnMap = getMetaInfo(clazz).getFiledNameColumnMap();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (filedNameColumnMap.containsKey(fieldName)) {
                        Object value = field.get(obj);
                        if (withNull || value != null) {
                            map.put(filedNameColumnMap.get(fieldName), value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get object field value error !");
        }
        return map;
    }


    /**
     * 获取对象的某个属性值
     *
     * @param o
     * @param property
     * @return
     */
    public static Object getObjValue(Object o, String property) {
        if (o == null)
            return null;
        try {
            Field declaredField = o.getClass().getDeclaredField(property);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        } catch (Exception e) {
            throw new RuntimeException("get object property error !");
        }
    }

    /**
     * 给对象的某个属性赋值
     *
     * @param o
     * @param property
     * @param value
     */
    public static void setObjValue(Object o, String property, Object value) {
        if (o == null)
            return;
        try {
            Field declaredField = o.getClass().getDeclaredField(property);
            declaredField.setAccessible(true);
            declaredField.set(o, value);
        } catch (Exception e) {
            throw new RuntimeException("set object proeperty error !");
        }
    }


    public static void setObjValues(Object o, Map<String, Object> values) {
        if (o == null)
            return;
        try {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Field declaredField = o.getClass().getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                declaredField.set(o, entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("set object proeperty error !");
        }
    }
}
