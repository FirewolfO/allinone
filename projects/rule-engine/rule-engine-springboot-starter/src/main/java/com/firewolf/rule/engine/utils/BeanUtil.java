package com.firewolf.rule.engine.utils;

import com.firewolf.rule.engine.core.QueryVO;
import org.apache.commons.collections4.MapUtils;

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
        if (o != null) {
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
        }
        return datas;
    }


    /**
     * 把对象转成Set，这个对象必须是List，Set，或者数组
     *
     * @param o
     * @return
     */
    public static Set transObj2Set(Object o) {
        Set datas = null;
        if (o.getClass().isArray()) {
            if (Array.getLength(o) > 0) {
                datas = new HashSet();
                for (int i = 0; i < Array.getLength(o); i++) {
                    datas.add(Array.get(o, i));
                }
            }
        } else if (o instanceof Collection) {
            datas = new HashSet((Collection) o);
        }
        return datas;
    }

    /**
     * 把对象转成集合，这个对象必须是List，Set，或者数组
     *
     * @param o
     * @return
     */
    public static Collection transObj2Collection(Object o) {
        Collection datas = null;
        if (o != null) {
            if (o.getClass().isArray()) {
                if (Array.getLength(o) > 0) {
                    datas = new ArrayList();
                    for (int i = 0; i < Array.getLength(o); i++) {
                        datas.add(Array.get(o, i));
                    }
                }
            } else if (o instanceof Collection) {
                datas = (Collection) o;
            }
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

    /**
     * 把集合转换成map
     * key-> 集合中元素的属性，value->属性对应值的合并
     *
     * @return
     */
    public static Map<String, Set> collectionToMap() {
        return null;
    }

    /**
     * 合并两个map的key对应的value，去掉空只，返回的是key- > Set的形式
     *
     * @param map1
     * @param map2
     */
    public static void joinMap(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1 == null || MapUtils.isEmpty(map2))
            return;
        map2.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object value = map2.get(key);
            if (value == null) {// 没有值的键值对丢弃
                return;
            }
            if (!map1.containsKey(key)) { // 不存在
                map1.put(key, new HashSet<>());
            } else {
                Object o = map1.get(key);
                if (isArrayOrCollection(o)) {
                    map1.put(key, transObj2Set(o));
                } else {
                    Set s = new HashSet();
                    s.add(o);
                    map1.put(key, s);
                }
            }
            Set valueSet = (Set) map1.get(key);
            if (isArrayOrCollection(value)) {
                valueSet.addAll(transObj2Set(value));
            } else {
                valueSet.add(value);
            }

        });
    }


    /**
     * 判断数据是不是数组或者Collection
     *
     * @param o
     * @return
     */
    public static boolean isArrayOrCollection(Object o) {
        if (o == null) {
            return false;
        }
        return (o instanceof Collection) || o.getClass().isArray();
    }

    /**
     * 获取某个
     *
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getFileValue(Object o, String fieldName) {
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
