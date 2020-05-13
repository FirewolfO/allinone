package com.firewolf.pattern.adapter.bothway;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 10:53 下午
 */
public class Client {

    public static void main(String[] args) {
        Target target = new Adapter(new ConcreteAdaptee());

        String sayHelloUpCase = target.sayHelloUpCase("zhangsan");

        System.out.println(sayHelloUpCase);
    }
}
