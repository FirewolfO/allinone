package com.firewolf.rule.engine.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class BeanUtil {

    /**
     * 把对象转型集合，这个对象必须是List，Set，或者数组
     *
     * @param o
     * @return
     */
    public static List transObj2List(Object o) {
        List datas = null;
        if (o.getClass().isArray()) {
            if (Array.getLength(o) > 0) {
                datas = new ArrayList();
                for (int i = 0; i < Array.getLength(o); i++) {
                    datas.add(Array.get(o, i));
                }
            }
        } else if (o instanceof Collection) {
            datas = new ArrayList((Collection) o);
        }
        return datas;
    }


    /**
     * 将对象属性转成Map结构
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
