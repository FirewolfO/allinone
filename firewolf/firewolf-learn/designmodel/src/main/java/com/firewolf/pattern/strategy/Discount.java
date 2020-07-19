package com.firewolf.pattern.strategy;

/**
 * Description: 折扣，充当抽象策略
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:18 上午
 */
public interface Discount {
    double calculate(double price);
}
