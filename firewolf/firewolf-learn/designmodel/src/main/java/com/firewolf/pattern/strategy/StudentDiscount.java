package com.firewolf.pattern.strategy;

/**
 * Description: 具体的策略类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:23 上午
 */
public class StudentDiscount implements Discount {
    @Override
    public double calculate(double price) {
        return price * 0.8;
    }
}
