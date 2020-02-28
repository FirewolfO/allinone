package com.firewolf.lx.tools;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.firewolf.lx.domain.RequestData;

/**
 * Author: liuxing
 * Date: 2020/2/24 21:13
 * 请求线程相关的数据
 */
public class RequestThreadLocal {

    private static final TransmittableThreadLocal<RequestData> threadLocal = new TransmittableThreadLocal<>();

    /**
     * 设置数据
     *
     * @param data
     */
    public static void set(RequestData data) {
        threadLocal.set(data);
    }

    /**
     * 删除数据
     */
    public static void remove() {
        threadLocal.remove();
    }


    /**
     * 获取数据
     *
     * @return
     */
    public static RequestData get() {
        return threadLocal.get();
    }
}
