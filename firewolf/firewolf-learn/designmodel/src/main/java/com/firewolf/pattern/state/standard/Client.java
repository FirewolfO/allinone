package com.firewolf.pattern.state.standard;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/16
 */
public class Client {

    public static void main(String[] args) {
        Account account = new Account("zhangsna");
        account.display();

        account.saveMoney(30);
        account.display();

        account.saveMoney(80);
        account.display();

        account.getMoney(30);
        account.display();

    }
}
