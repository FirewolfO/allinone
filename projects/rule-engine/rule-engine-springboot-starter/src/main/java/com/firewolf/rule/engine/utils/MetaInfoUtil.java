package com.firewolf.rule.engine.utils;

import com.firewolf.rule.engine.annotations.*;
import com.firewolf.rule.engine.entity.EntityMetaInfo;
import com.firewolf.rule.engine.enums.UniqueType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
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
                        metaInfo.getColumnFieldNameMap().put(columnName, fieldName);
                        metaInfo.getFiledNameColumnMap().put(fieldName, columnName);
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
                        metaInfo.getOrderColumnMap().put(columnName, orderBy.value());
                    }

                    if (field.getDeclaredAnnotation(Like.class) != null) {
                        Like like = field.getDeclaredAnnotation(Like.class);
                        metaInfo.getLikeColumnMap().put(columnName, like.value());
                    }

                    if (field.getDeclaredAnnotation(UniqueCheck.class) != null) {
                        UniqueCheck like = field.getDeclaredAnnotation(UniqueCheck.class);
                        if (like.value() == UniqueType.Union) {
                            metaInfo.getUnionKeyColumns().add(columnName);
                        } else {
                            metaInfo.getUniqueKeyColumns().add(columnName);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            });
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
            throw new RuntimeException("set object property error !");
        }
    }
}
