package com.firewolf.pattern.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：享元工厂
 * Author：liuxing
 * Date：2020/7/6
 */
public class IgoChessmanFactory {

    private static final Map<String, IgoChessman> pool = new ConcurrentHashMap<>();

    static {
        pool.put("black", new BlackIgoChessman());
        pool.put("white", new WhitIgoChessman());
    }
    
    public static IgoChessman getIgoChessman(String color) {
        return pool.get(color);
    }
}
