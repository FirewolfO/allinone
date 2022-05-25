package com.firewolf.pattern.strategy;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:25 上午
 */
public class Client {

    public static void main(String[] args) {
        MovieTicket movieTicket = new MovieTicket();
        movieTicket.setPrice(100);

        movieTicket.setDiscount(new VipDiscount());
        System.out.println("Vip 价格为：" + movieTicket.getPrice());

        movieTicket.setDiscount(new StudentDiscount());
        System.out.println("学生 价格为：" + movieTicket.getPrice());
    }
}
