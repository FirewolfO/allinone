package com.firewolf.log.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean操作工具类
 */
public class BeanUtil {


    /**
     * 获取对象属性键值对
     *
     * @param o
     * @return
     */
    public static Map<String, Object> getValueMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (o != null) {
                Field[] fields = o.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(o));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("get object filed Value error !", e);
        }
        return map;
    }


    /**
     * 获取某个对象的某个属性值
     *
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getValue(Object o, String fieldName) {
        if (o == null || fieldName == null) {
            return null;
        }
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
