package com.firewolf.pattern.strategy;

/**
 * Description: 具体策略类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:24 上午
 */
public class VipDiscount implements Discount {
    @Override
    public double calculate(double price) {
        return price * 0.5;
    }
}
