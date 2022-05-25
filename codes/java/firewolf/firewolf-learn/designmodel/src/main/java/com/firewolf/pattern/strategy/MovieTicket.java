package com.firewolf.pattern.strategy;

import lombok.Data;

/**
 * Description: 电影票，充当环境类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:17 上午
 */
@Data
public class MovieTicket {
    /**
     * 价格
     */
    private double price;
    /**
     * 抽象的打折策略
     */
    private Discount discount;

    public double getPrice() {
        return discount.calculate(price);
    }
}
