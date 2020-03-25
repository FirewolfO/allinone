package com.firewolf.rule.engine.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

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
     * 判断数据是不是数组或者Collection
     *
     * @param list  列表对象
     * @param clazz 具体类型，这里支持支List,Set,Array
     * @return
     */
    public static Object transList2Obj(List list, Class<?> clazz) {
        if (clazz == Set.class) {
            return new HashSet<>(list);
        } else if (clazz == List.class) {
            return list;
        } else if (clazz.isArray()) {
            return list.toArray();
        }
        return list;
    }

}
